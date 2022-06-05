import os
import math
from multipledispatch import dispatch


def finish():
  print('Программа завершила работу.\n')
  os.system("pause")
  exit()


print('Есть три функции:')
print(' 1. cos(x) - x')
print(' 2. x³ - 3,78x² + 1,25x + 3,49')
print(' 3. e^x - x³')

try:
  functionNumber = int(input('\nВведите номер функции, на интервале которой необходимо вычислить интеграл: '))
except ValueError:
  print('Ошибка! Номером функции должно быть натуральное число.')
  finish()
if functionNumber < 1 or functionNumber > 3:
  print('Ошибка! Номером функции должно быть натуральное число от 1 до 3.')
  finish()

print('\nДва способа приближённого вычисления интеграла:')
print(' 1. Метод трапеций')
print(' 2. Метод Симпсона')

try:
  methodNumber = int(input('\nВведите номер метода, который будет использоваться для вычисления интеграла: '))
except ValueError:
  print('Ошибка! Номером метода должно быть натуральное число.')
  finish()
if methodNumber < 1 or methodNumber > 2:
  print('Ошибка! Номером метода должна быть единица или двойка.')
  finish()

try:
  admissibleError = float(input('\nВведите точность (погрешность) вычислений: '))
except ValueError:
  print('Ошибка! В качестве точности должно быть указано число.')
  finish()
if admissibleError <= 0:
  print('Ошибка! Точность должна быть положительным числом.')
  finish()

try:
  leftBorder = float(input('\nВведите левую границу интервала интегрирования: '))
except ValueError:
  print('Ошибка! Левой границей интервала интегрирования должно быть число.')
  finish()

try:
  rightBorder = float(input('\nВведите правую границу интервала интегрирования: '))
except ValueError:
  print('Ошибка! Правой границей интервала интегрирования должно быть число.')
  finish()


if functionNumber == 1:
  func = lambda x: math.cos(x) - x
elif functionNumber == 2:
  func = lambda x: x**3 - 3.78 * x**2 + 1.25 * x + 3.49
else:
  func = lambda x: math.exp(x) - x**3


@dispatch(object, float, float, int)
def calculateIntegralTrapezoidMethod(func, a, b, n):
  h = (b - a) / n
  x = a
  sum = 0
  for i in range(n - 2):
    x += h
    sum += func(x)
  return (sum * 2 + func(a) + func(b)) * h / 2

@dispatch(object, float, float, float, int)
def calculateIntegralTrapezoidMethod(func, a, b, admissibleError, n):
  integral = calculateIntegralTrapezoidMethod(func, a, b, n)
  while True:
    n *= 2
    newIntegral = calculateIntegralTrapezoidMethod(func, a, b, n)
    if abs(newIntegral - integral) / 3 < admissibleError:
      return (newIntegral, n)
    integral = newIntegral


@dispatch(object, float, float, int)
def calculateIntegralSimpsonMethod(func, a, b, n):
  integral = func(a) + func(b)
  h = (b - a) / n
  
  x = a + h
  h *= 2
  sum = 0
  for i in range((n - 2) // 2 + 1):
    sum += func(x)
    x += h
  integral += sum * 4
  
  x = a + h
  sum = 0
  for i in range((n - 2) // 2):
    sum += func(x)
    x += h
  integral += sum * 2
  
  return integral * h / 6

@dispatch(object, float, float, float, int)
def calculateIntegralSimpsonMethod(func, a, b, admissibleError, n):
  integral = calculateIntegralSimpsonMethod(func, a, b, n)
  while True:
    n *= 2
    newIntegral = calculateIntegralSimpsonMethod(func, a, b, n)
    if abs(newIntegral - integral) / 15 < admissibleError:
      return (newIntegral, n)
    integral = newIntegral

result = calculateIntegralTrapezoidMethod(func, leftBorder, rightBorder, admissibleError, 4) if methodNumber == 1 \
    else calculateIntegralSimpsonMethod(func, leftBorder, rightBorder, admissibleError, 4)

print('\nНайденное значение интеграла:', result[0])
print('Число разбиения интервала интегрирования:', result[1], '\n')

finish()