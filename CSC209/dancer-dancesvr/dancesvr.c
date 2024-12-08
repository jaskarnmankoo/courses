#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

int port = 1234;
int listenfd;
char banner[] = "dancecard 1\r\n";

static char handleMessage[] = "Sorry, that word is a command, so it can't be used as a handle.\r\n";
static char roleMessage[] = "Invalid role. Type lead, follow, or both.\r\n";
static char welcomeMessage[] = "Welcome to the dance!\r\n";
static char noOneHereMessage[] = "No one else is here yet, but I'm sure they'll be here soon!\r\n";
static char upMessage[] = "Unpartnered dancers are:\r\n";
static char dupMessage[] = "Sorry, someone is already using that handle.  Please choose another.\r\n";
static char noNameMessage[] = "There is no dancer by that name.\r\n";
static char couplesMessage[] = "This is a couples dance -- you can't dance with yourself.\r\n";
static char sorryMessage[] = "I'm sorry, your partner seems to have left the dance hall!\nYou'll need to find someone else to dance with.\r\n";
static char alreadyHookedMessage[] = "You already have a partner for this dance!  Type 'begin' to ask for the dance to begin.\r\n";

#define MAXHANDLE 50 /*maximum permitted handle size, not including \0 */
#define FOLLOW 0
#define LEAD 1
#define BOTH 2

struct dancer
{
	int fd;
	struct in_addr ipaddr;
	char handle[MAXHANDLE + 1]; /*zero-terminated; handle[0]==0 if not set */
	int role;					/*-1 if not set yet */
	char buf[200];				/*data in progress */
	int bytes_in_buf;			/*how many data bytes already read in buf */
	struct dancer *partner;		/*null if not yet partnered */
	struct dancer *next;
} *dancers = NULL;

int nlead = 0, nfollow = 0, nboth = 0, someone_is_partnered = 0;

extern void parseargs(int argc, char **argv);
extern void makelistener();
extern void newclient(int fd, struct sockaddr_in *r);
extern void clientactivity(struct dancer *p);
extern void removeclient(struct dancer *p);
extern void whoCase(struct dancer *p);
extern void joinedCase(struct dancer *p);
extern void roleIncDec(struct dancer *p, int type);
extern int xHelper();
extern void do_something(struct dancer *p, char *wherenewline);
extern void begindance();
extern char *memnewline(char *p, int size); /*finds \r _or_ \n */

int main(int argc, char **argv)
{
	int did_something;
	struct dancer *p, *n;

	parseargs(argc, argv);
	makelistener();

	while (1)
	{
		struct sockaddr_in r;
		fd_set fds;
		int maxfd = listenfd;
		FD_ZERO(&fds);
		FD_SET(listenfd, &fds);
		did_something = 0;

		for (p = dancers; p; p = p->next)
		{
			FD_SET(p->fd, &fds);

			if (p->fd > maxfd)
			{
				maxfd = p->fd;
			}
		}

		/*check if we have a full input line in memory for some client */
		for (p = dancers; p; p = n)
		{
			char *q = memnewline(p->buf, p->bytes_in_buf);
			n = p->next; /*stash the pointer now in case 'p' is deleted */

			if (q)
			{
				do_something(p, q);
				did_something = 1;
			}
		}

		if (!did_something)
		{
			/*nothing left which has already been read -- call select */
			if (select(maxfd + 1, &fds, NULL, NULL, NULL) < 0)
			{
				perror("select");
			}
			else
			{
				for (p = dancers; p; p = p->next)
				{
					if (FD_ISSET(p->fd, &fds))
					{
						break;
					}
				}

				if (p)
				{
					clientactivity(p);
				}

				if (FD_ISSET(listenfd, &fds))
				{
					newclient(listenfd, &r);
				}
			}
		}
	}
}

void parseargs(int argc, char **argv)
{
	int c, status = 0;
	while ((c = getopt(argc, argv, "p:")) != EOF)
	{
		switch (c)
		{
		case 'p':
			port = atoi(optarg);
			break;
		default:
			status++;
		}
	}

	if (status || optind != argc)
	{
		fprintf(stderr, "usage: %s[-p port]\n", argv[0]);
		exit(1);
	}
}

void makelistener()
{
	struct sockaddr_in r;

	if ((listenfd = socket(AF_INET, SOCK_STREAM, 0)) < 0)
	{
		perror("socket");
		exit(1);
	}

	memset(&r, '\0', sizeof r);
	r.sin_family = AF_INET;
	r.sin_addr.s_addr = INADDR_ANY;
	r.sin_port = htons(port);

	if (bind(listenfd, (struct sockaddr *)&r, sizeof r) < 0)
	{
		perror("bind");
		exit(1);
	}

	if (listen(listenfd, 5))
	{
		perror("listen");
		exit(1);
	}
}

void newclient(int fd, struct sockaddr_in *r)
{
	socklen_t socklen = sizeof r;

	if ((fd = accept(listenfd, (struct sockaddr *)r, &socklen)) < 0)
	{
		perror("accept");
	}
	else
	{
		printf("Connection from %s\n", inet_ntoa(r->sin_addr));
		struct dancer *p = malloc(sizeof(struct dancer));

		if (!p)
		{
			fprintf(stderr, "out of memory!\n");
			exit(1);
		}

		fflush(stdout);
		p->fd = fd;
		p->handle[0] = '\0';
		p->ipaddr = r->sin_addr;
		p->role = -1;
		p->partner = NULL;
		p->next = dancers;
		dancers = p;
		write(p->fd, banner, sizeof banner - 1);
	}
}

void clientactivity(struct dancer *p)
{
	char *q;
	int len;
	struct dancer *n;
	char bid[200];

	// do an appropriate read() into p->buf, given the existing p->bytes_in_buf
	len = read(p->fd, p->buf, sizeof p->buf);
	p->bytes_in_buf += len;

	if (len == 0)
	{
		for (n = dancers; n; n = n->next)
		{
			if (strcmp(p->handle, n->handle) != 0)
			{
				sprintf(bid, "%s bids you all good night.\r\n", p->handle);
				write(n->fd, bid, strlen(bid));
			}
		}

		if (p->partner != NULL)
		{
			write(p->partner->fd, sorryMessage, sizeof sorryMessage - 1);
		}

		removeclient(p);
	}
	else
	{
		if ((q = memnewline(p->buf, p->bytes_in_buf)))
		{
			do_something(p, q);
		}
		else if (p->bytes_in_buf == sizeof p->buf)
		{
			/*
			 *We don't have a newline, but our buffer is full, so we'd
			 *better process it anyway.
			 */
			do_something(p, p->buf + sizeof p->buf - 1);
		}
	}
}

void removeclient(struct dancer *p)
{
	struct dancer **n;
	for (n = &dancers; *n && (*n)->fd != p->fd; n = &(*n)->next)
	{
		;
	}

	if (*n)
	{
		p = (*n)->next;
		printf("disconnecting client %s\n", inet_ntoa((*n)->ipaddr));
		fflush(stdout);
		free(*n);
		*n = p;
	}
}

void whoCase(struct dancer *p)
{
	struct dancer *n = dancers;

	if (n->next == NULL)
	{
		write(p->fd, noOneHereMessage, sizeof noOneHereMessage - 1);
	}
	else
	{
		write(p->fd, upMessage, sizeof upMessage - 1);

		for (n = dancers; n; n = n->next)
		{
			static char message[100];
			static char buf[MAXHANDLE + 1];
			if (n->partner == NULL && strcmp(n->handle, p->handle) != 0 && n->handle[0] != '\0' && n->role != -1)
			{
				if (p->role == n->role && p->role != BOTH)
				{
					if (p->role == LEAD)
					{
						sprintf(message, "[%s only dances lead]\r\n", n->handle);
						write(p->fd, message, strlen(message));
					}
					else
					{
						sprintf(message, "[%s only dances follow]\r\n", n->handle);
						write(p->fd, message, strlen(message));
					}
				}
				else
				{
					sprintf(buf, "%s\r\n", n->handle);
					write(p->fd, buf, strlen(buf));
				}
			}
		}
	}
}

void joinedCase(struct dancer *p)
{
	struct dancer *n;
	for (n = dancers; n; n = n->next)
	{
		static char buffer[100];
		if (strcmp(n->handle, p->handle) != 0 && n->handle[0] != '\0' && n->role != -1)
		{
			sprintf(buffer, "%s has joined the dance\r\n", p->handle);
			write(n->fd, buffer, strlen(buffer));
		}
	}
}

/*there is a command in the buffer; do it */
void do_something(struct dancer *p, char *wherenewline)
{
	int len;
	len = wherenewline - p->buf;
	*wherenewline = '\0';
	struct dancer *n;

	if (len == 0)
	{
		; /*ignore blank lines */
	}
	else if (p->handle[0] == '\0')
	{
		int truth = 1;
		for (n = dancers; n; n = n->next)
		{
			if (strcmp(n->handle, p->buf) == 0)
			{
				fprintf(stderr, "refusing to accept handle '%s'\n", p->buf);
				write(p->fd, dupMessage, sizeof dupMessage - 1);
				truth = 0;
			}
		}

		if (truth)
		{
			if (strcmp(p->buf, "who") == 0 || strcmp(p->buf, "begin") == 0 || strcmp(p->buf, "debug") == 0)
			{
				fprintf(stderr, "refusing to accept handle '%s'\n", p->buf);
				write(p->fd, handleMessage, sizeof handleMessage - 1);
			}
			else
			{
				strcpy(p->handle, p->buf);
				printf("Set handle of fd %d to %s\n", p->fd, p->buf);
				write(p->fd, "\r\n", 1);
			}
		}
	}
	else if (p->role == -1)
	{
		if (p->buf[0] == 'f' || p->buf[0] == 'l' || p->buf[0] == 'b')
		{
			if (p->buf[0] == 'f')
			{
				nfollow++;
				p->role = FOLLOW;
				printf("Set role of fd %d to %d\n", p->fd, FOLLOW);
			}
			else if (p->buf[0] == 'l')
			{
				nlead++;
				p->role = LEAD;
				printf("Set role of fd %d to %d\n", p->fd, LEAD);
			}
			else
			{
				nboth++;
				p->role = BOTH;
				printf("Set role of fd %d to %d\n", p->fd, BOTH);
			}

			write(p->fd, "\r\n", 1);
			write(p->fd, welcomeMessage, sizeof welcomeMessage - 1);
			whoCase(p);
			joinedCase(p);
		}
		else
		{
			write(p->fd, roleMessage, sizeof roleMessage - 1);
		}
	}
	else if (strcmp(p->buf, "who") == 0)
	{
		whoCase(p);
	}
	else if (strcmp(p->buf, "begin") == 0)
	{
		begindance();
	}
	else
	{
		int type;
		static char nbuf[200];
		static char pbuf[200];
		static char xError[200];

		for (n = dancers; n; n = n->next)
		{
			if (strcmp(p->buf, n->handle) == 0)
			{
				if (strcmp(p->handle, n->handle) == 0)
				{
					type = 0;
					break;
				}
				else
				{
					type = 1;
					break;
				}
			}
			else
			{
				type = 2;
			}
		}

		if (type == 0)
		{
			write(p->fd, couplesMessage, sizeof couplesMessage - 1);
		}
		else if (type == 1)
		{
			if (n->partner == NULL)
			{
				if (n->role != p->role || n->role == BOTH)
				{
					roleIncDec(p, 0);
					roleIncDec(n, 0);
					int x = xHelper();
					;

					if (x == 0)
					{
						someone_is_partnered++;
						n->partner = p;
						p->partner = n;
						sprintf(pbuf, "%s accepts!\r\n", n->handle);
						write(p->fd, pbuf, strlen(pbuf));
						sprintf(nbuf, "%s has asked you to dance.  You accept!\r\n", p->handle);
						write(n->fd, nbuf, strlen(nbuf));
						if (someone_is_partnered >= 1 && nlead == 0 && nfollow == 0 && nboth == 0)
						{
							begindance();
						}
					}
					else
					{
						roleIncDec(p, 1);
						roleIncDec(n, 1);
						sprintf(xError, "Please don't ask %s to dance, as that would leave people out.\r\n", n->handle);
						write(p->fd, xError, strlen(xError));
					}
				}
				else
				{
					static char sry[200];

					if (p->role == FOLLOW)
					{
						sprintf(sry, "Sorry, %s can only dance follow.\r\n", n->handle);
						write(p->fd, sry, strlen(sry));
					}
					else if (p->role == LEAD)
					{
						sprintf(sry, "Sorry, %s can only dance lead.\r\n", n->handle);
						write(p->fd, sry, strlen(sry));
					}
				}
			}
			else if (p->partner != NULL)
			{
				write(p->fd, alreadyHookedMessage, sizeof alreadyHookedMessage - 1);
			}
			else
			{
				sprintf(pbuf, "%s already has a partner for this dance.  Try again next dance!\r\n", n->handle);
				write(p->fd, pbuf, strlen(pbuf));
			}
		}
		else
		{
			write(p->fd, noNameMessage, sizeof noNameMessage - 1);
		}
	}

	// ... etc...

	/*now remove this command from the buffer */
	/*p->buf[len] was either \r or \n.  How about p->buf[len+1]? */
	len++;

	if (len < p->bytes_in_buf && (p->buf[len] == '\r' || p->buf[len] == '\n'))
	{
		len++;
	}

	p->bytes_in_buf -= len;
	memmove(p->buf, p->buf + len, p->bytes_in_buf);
}

int xHelper()
{
	int x = abs(nlead - nfollow) - nboth;

	if (x < 0)
	{
		return 0;
	}

	return x;
}

void roleIncDec(struct dancer *p, int type)
{
	if (type)
	{
		if (p->role == LEAD)
		{
			nlead++;
		}
		else if (p->role == FOLLOW)
		{
			nfollow++;
		}
		else if (p->role == BOTH)
		{
			nboth++;
		}
	}
	else
	{
		if (p->role == LEAD)
		{
			nlead--;
		}
		else if (p->role == FOLLOW)
		{
			nfollow--;
		}
		else if (p->role == BOTH)
		{
			nboth--;
		}
	}
}

void begindance()
{
	static char message1[] = "Dance begins\r\n";
	static char message2[] = "Dance ends.  Your partner thanks you for the dance!\r\nTime to find a new partner.  Type 'who' for a list of available dancers.\r\n";
	struct dancer *p;

	for (p = dancers; p; p = p->next)
	{
		write(p->fd, message1, sizeof message1 - 1);
	}

	sleep(5);

	for (p = dancers; p; p = p->next)
	{
		write(p->fd, message2, sizeof message2 - 1);
		p->partner = NULL;
	}

	someone_is_partnered = 0;
	// something to reset nlead, etc --
	//   I suggest counting again from scratch, probably as a separate function
	nlead = 0;
	nfollow = 0;
	nboth = 0;
	struct dancer *n;
	for (n = dancers; n; n = n->next)
	{
		if (n->role == FOLLOW)
		{
			nfollow++;
		}
		else if (n->role == LEAD)
		{
			nlead++;
		}
		else if (n->role == BOTH)
		{
			nboth++;
		}

		n->partner = NULL;
	}
}

char *memnewline(char *p, int size)
{
	/*finds \r _or_ \n */
	/*This is like min(memchr(p, '\r'), memchr(p, '\n')) */
	/*It is named after memchr().  There's no memcspn(). */
	for (; size > 0; p++, size--)
	{
		if (*p == '\r' || *p == '\n')
		{
			return (p);
		}
	}
	return (NULL);
}
