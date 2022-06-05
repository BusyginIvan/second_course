/* Загрузка и сохранение изображений формата bmp. */

#ifndef BMP_H
#define BMP_H

#include "image.h"
#include <stdio.h>

enum bmp_error_code {
	BMP_INVALID_HEADER = 0,
	BMP_ANOTHER_ERROR,
	BMP_SUCCESS
};

enum bmp_error_code bmp_to_img(FILE* file, struct image* img);
enum bmp_error_code img_to_bmp(FILE* file, struct image img);

#endif
