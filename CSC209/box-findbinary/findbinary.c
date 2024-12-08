#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <dirent.h>
#include <string.h>

int findbinary(char *path, int err)
{
	int i, c;
	int comp[4] = {
		127,
		'E',
		'L',
		'F'};
	char buf[2000];
	struct stat statbuf;
	i = 0;

	if (lstat(path, &statbuf))
	{
		perror(path);
		return 1;
	}
	else
	{
		if (S_ISREG(statbuf.st_mode))
		{
			if (err == 0)
			{
				fprintf(stderr, "%s: Not a directory\n", path);
				return 1;
			}

			FILE *file = fopen(path, "rb");

			if (file == NULL)
			{
				perror("fopen");
				return 1;
			}

			while (((c = getc(file)) != EOF) && i < 4)
			{
				if (c != comp[i])
				{
					break;
				}

				i++;
			}

			if (i == 4)
			{
				printf("%s\n", path);
			}

			fclose(file);
		}
		else if (S_ISDIR(statbuf.st_mode))
		{
			err++;
			DIR *dp = opendir(path);
			struct dirent *p;

			if (dp == NULL)
			{
				perror(path);
				return 1;
			}
			else
			{
				while ((p = readdir(dp)))
				{
					if ((p->d_name[0] == '.' || p->d_name[1] == '.') && strlen(p->d_name) <= 2)
					{
						continue;
					}
					else
					{
						strcpy(buf, path);
						strcat(buf, "/");
						strcat(buf, p->d_name);
						if (buf[1999] == '\0')
						{
							findbinary(buf, err);
						}
						else
						{
							fprintf(stderr, "path name too long\n");
							return 1;
						}
					}
				}
			}

			closedir(dp);
		}
	}

	return 0;
}

int main(int argc, char **argv)
{
	int er = 0;

	if (argc != 2)
	{
		fprintf(stderr, "usage: %s path\n", argv[0]);
		return 1;
	}

	return findbinary(argv[1], er);
}
