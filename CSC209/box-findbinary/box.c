#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

static char *border = "*";
static int width = 70;
static int eflag = 0;

void row(char border)
{
	int i;

	for (i = 0; i < width; i++)
	{
		putchar(border);
	}

	putchar('\n');
}

void process(FILE *fp, char border)
{
	int c, numc;
	numc = 0;

	while ((c = getc(fp)) != EOF)
	{
		if (numc == 0)
		{
			putchar(border);
			putchar(' ');
		}

		if (c == '\n')
		{
			while (numc < width - 4)
			{
				putchar(' ');
				numc++;
			}

			putchar(' ');
			putchar(border);
			putchar(c);
			numc = 0;
		}
		else
		{
			if (eflag)
			{
				putchar(c);
				numc++;
			}
			else
			{
				if (numc < width - 4)
				{
					putchar(c);
					numc++;
				}
			}
		}
	}
}

int main(int argc, char **argv)
{
	int i, c;
	FILE *fp;

	while ((c = getopt(argc, argv, "c:ew:")) != EOF)
	{
		switch (c)
		{
		case 'c':
			border = strdup(optarg);
			border[1] = '\0';
			break;
		case 'e':
			eflag = 1;
			break;
		case 'w':
			width = atoi(optarg);
			break;
		default:
			fprintf(stderr, "usage %s[-e][-w width][-c char][file ...]\n", argv[0]);
			return 1;
		}
	}

	row(*border);

	if (optind == argc)
	{
		process(stdin, *border);
	}
	else
	{
		for (i = optind; i < argc; i++)
		{
			if (strcmp(argv[i], "-") == 0)
			{
				process(stdin, *border);
			}
			else
			{
				if ((fp = fopen(argv[i], "r")) == NULL)
				{
					perror(argv[i]);
					return 1;
				}

				process(fp, *border);
				fclose(fp);
			}
		}
	}

	row(*border);
	return 0;
}
