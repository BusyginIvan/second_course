/* Открытие и закрытие файлов. */

#include "files.h"
#include <errno.h>

enum files_error_code open_file(FILE** file, const char* name, const char* mode) {
	*file = fopen(name, mode);
	if (*file != NULL) return FL_SUCCESS;
	if (errno == ENOENT) return FL_FILE_NOT_EXIST;
	if (errno == EACCES) return FL_PERMISSION_DENIED;
	return FL_ANOTHER_ERROR;
}
