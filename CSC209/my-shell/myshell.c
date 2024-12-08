#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <sys/types.h>

int main(int argc, char **argv)
{
	int c, i, j, x, size, anotherNL, newCmd, count, truth;
	j = 0;
	anotherNL = 0;
	newCmd = 1;
	truth = 1;
	size = 10;
	struct stat statbuf;
	extern char **environ;
	char **args = malloc(sizeof(char *) * size);

	while (truth)
	{
		if (newCmd)
		{
			for (i = 0; i < size; i++)
			{
				args[i] = malloc(sizeof(char) * 1000);
			}

			i = 0;
		}

		c = getchar();

		if (c == EOF)
		{
			truth = 0;
		}

		if ((c == '\n' || c == EOF) && anotherNL)
		{
			args[i] = NULL;
			anotherNL = 0;
			newCmd = 1;
			i = 0;
			count = 0;
			int s = strlen(args[0]);
			x = fork();

			if (x == -1)
			{
				perror("fork");
				exit(1);
			}
			else if (x == 0)
			{
				if (args[0][0] == '/' || args[0][0] == '.')
				{
					if (stat(args[0], &statbuf) == 0)
					{
						execve(args[0], args, environ);
						exit(0);
					}
					else
					{
						perror(args[0]);
						exit(1);
					}
				}

				char *pathA = malloc(sizeof(char) * (5 + s));
				strcpy(pathA, "/bin/");
				strcat(pathA, args[0]);
				char *pathB = malloc(sizeof(char) * (9 + s));
				strcpy(pathB, "/usr/bin/");
				strcat(pathB, args[0]);
				char *pathC = malloc(sizeof(char) * (2 + s));
				strcpy(pathC, "./");
				strcat(pathC, args[0]);

				if (stat(pathA, &statbuf) == 0)
				{
					execve(pathA, args, environ);
				}
				else if (stat(pathB, &statbuf) == 0)
				{
					execve(pathB, args, environ);
				}
				else if (stat(pathC, &statbuf) == 0)
				{
					execve(pathC, args, environ);
				}
				else
				{
					perror(args[0]);
					exit(1);
				}

				free(pathA);
				free(pathB);
				free(pathC);
				exit(0);
			}
			else
			{
				int status;
				wait(&status);

				if (WIFEXITED(status))
				{
					WEXITSTATUS(status);

					for (i = 0; i < size; i++)
					{
						free(args[i]);
					}
				}
				else
				{
					return (1);
				}
			}
		}
		else if (c == '\n' && !anotherNL)
		{
			newCmd = 0;

			if (count == 0)
			{
				continue;
			}

			args[i][j] = '\0';
			i++;
			j = 0;
			anotherNL = 1;

			if (i >= size)
			{
				int temp = 0;
				size += 10;

				args = realloc(args, sizeof(char *) * size);

				if (args == NULL)
				{
					perror("realloc");
					exit(1);
				}

				for (temp = i; temp < size; temp++)
				{
					args[temp] = malloc(sizeof(char) * 1000);
				}
			}
		}
		else
		{
			args[i][j] = c;
			j++;
			count++;
			newCmd = 0;
			anotherNL = 0;
		}
	}

	free(args);
	return (0);
}
