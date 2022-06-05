/* Открытие и закрытие файлов. */

#ifndef FILES_H
#define FILES_H

#include <stdio.h>

enum files_error_code {
	FL_FILE_NOT_EXIST = 0,
	FL_PERMISSION_DENIED,
	FL_ANOTHER_ERROR,
	FL_SUCCESS
};

enum files_error_code open_file(FILE** file, const char* name, const char* mode);

#endif
