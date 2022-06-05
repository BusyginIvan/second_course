/* Сепиа фильтр для изображения. */

#ifndef SEPIA_H
#define SEPIA_H

#include "../include/image.h"

void sepia_without_sse(struct image* img);
void sepia_with_sse(struct image* img);

#endif
