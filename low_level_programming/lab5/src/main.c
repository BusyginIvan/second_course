/* Основная функция программы. */

#include <stdio.h>
#include <stdarg.h>
#include <stdlib.h>
#include <sys/time.h>
#include <sys/resource.h>

#include "bmp.h"
#include "files.h"
#include "image.h"
#include "sepia.h"

static const char* const files_error_messages[] = {
	"Ошибка! Не существует нужный файл или каталог.\n",
	"Ошибка! Нет нужных прав.\n",
	"Какая-то ошибка при работе с файлом.\n"
};

static const char* const bmp_error_messages[] = {
	"Ошибка! Содержимое файла не соответствует формату bmp.\n",
	"Какая-то ошибка при работе с форматом bmp.\n"
};

void err(const char* format, ...) {
  va_list args;
  va_start(args, format);
  vfprintf(stderr, format, args);
  va_end(args);
  abort();
}

int main(int argc, char* argv[]) {
  if (argc < 3)
    err("Для выполнения программы необходимо два аргумента командной строки: "
        "файл исходного изображения и файл, который будет создан.\n");

  FILE* file;
  enum files_error_code files_code = open_file(&file, argv[1], "rb");
	if (files_code != FL_SUCCESS)
    err(files_error_messages[files_code]);

  struct image img = { 0 };
  enum bmp_error_code bmp_code = bmp_to_img(file, &img);
  fclose(file);
	if (bmp_code != BMP_SUCCESS) {
    destruct_image(&img);
    err(bmp_error_messages[bmp_code]);
	}

  struct rusage r;
  getrusage(RUSAGE_SELF, &r);
  struct timeval start;
  start = r.ru_utime;

  const int n = 10; // Сколько раз применить фильтр (для тестирования).
  for (int i = 0; i < n; i++)
    sepia_without_sse(&img);

  getrusage(RUSAGE_SELF, &r);
  struct timeval end;
  end = r.ru_utime;

  long res = ((end.tv_sec - start.tv_sec) * 1000000L) + end.tv_usec - start.tv_usec;
  printf("На преобразование изображения в среднем было затрачено %ld микросекунд.\n", res / n);

  files_code = open_file(&file, argv[2], "wb");
  if (files_code != FL_SUCCESS) {
    destruct_image(&img);
    err(files_error_messages[files_code]);
  }

  bmp_code = img_to_bmp(file, img);
  fclose(file);
  destruct_image(&img);
  if (bmp_code != BMP_SUCCESS)
    err(bmp_error_messages[bmp_code]);

  printf("Изображение с применением фильтра успешно создано!\n");
	return 0;
}
