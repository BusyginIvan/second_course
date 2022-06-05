import os
import math
from multipledispatch import dispatch


def finish():
  print('\nПрограмма завершила работу.\n')
  os.system("pause")
  exit()


print('Есть две функции:')
print(' 1. ln(x)')
print(' 2. 1/x')

try:
  functionNumber = int(input('\nВведите номер функции, на интервале которой необходимо вычислить интеграл: '))
except ValueError:
  print('Ошибка! Номером функции должно быть натуральное число.')
  finish()
if functionNumber < 1 or functionNumber > 2:
  print('Ошибка! Номером функции должна быть единица или двойка.')
  finish()

print('\nДва способа приближённого вычисления интеграла:')
print(' 1. Метод трапеций')
print(' 2. Метод Симпсона')

try:
  methodNumber = int(input('\nВведите номер метода, который будет использоваться для вычисления интеграла: '))
except ValueError:
  print('\nОшибка! Номером метода должно быть натуральное число.')
  finish()
if methodNumber < 1 or methodNumber > 2:
  print('\nОшибка! Номером метода должна быть единица или двойка.')
  finish()

try:
  admissibleError = float(input('\nВведите точность (погрешность) вычислений: '))
except ValueError:
  print('\nОшибка! В качестве точности должно быть указано число.')
  finish()
if admissibleError <= 0:
  print('\nОшибка! Точность должна быть положительным числом.')
  finish()

try:
  leftBorder = float(input('\nВведите левую границу интервала интегрирования: '))
except ValueError:
  print('\nОшибка! Левой границей интервала интегрирования должно быть число.')
  finish()

try:
  rightBorder = float(input('\nВведите правую границу интервала интегрирования: '))
except ValueError:
  print('\nОшибка! Правой границей интервала интегрирования должно быть число.')
  finish()
if rightBorder <= leftBorder:
  print('\nОшибка! Значение правой границы интервала не может быть меньше значения левой границы.')
  finish()


def calculateSumTrapezoidMethod(func, a, b, n):
  h = (b - a) / n
  x = a
  sum = 0
  for i in range(n - 2):
    x += h
    sum += func(x)
  return (sum * 2 + func(a) + func(b)) * h / 2

def calculateSumSimpsonMethod(func, a, b, n):
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

def calculateIntegral(func, calculateSum, a, b, admissibleError, n, leftValueIsInfinity):
  d = (a + b) / n / 8
  itersAmout = 1
  integral = calculateSum(func, a + d if leftValueIsInfinity else a, b, n)
  while True:
    itersAmout += 1
    n *= 2
    d /= 2
    newIntegral = calculateSum(func, a + d if leftValueIsInfinity else a, b, n)
    if abs(newIntegral - integral) < admissibleError:
      return (newIntegral, n)
    if itersAmout > 20:
      print('Интеграл не был вычеслен за 30 итераций. Вы уверены, что он сходится?')
      finish()
    integral = newIntegral


leftValueIsInfinity = False

if functionNumber == 1:
  if leftBorder < 0:
    print('\nОшибка! Функция не определена на отрицательных значениях. Левая граница не может быть меньше нуля.')
    finish()
  if leftBorder == 0:
    leftValueIsInfinity = True
  func = lambda x: math.log(x)
else:
  #if leftBorder <= 0 and rightBorder >= 0:
  #  print('\nИнтеграл не сходится.')
  #  finish()
  if leftBorder == 0:
    leftValueIsInfinity = True
  func = lambda x: 1 / x

calculateSum = calculateSumTrapezoidMethod if methodNumber == 1 else calculateSumSimpsonMethod

result = calculateIntegral(func, calculateSum, leftBorder, rightBorder, admissibleError, 4, leftValueIsInfinity)

print('\nНайденное значение интеграла:', result[0])
print('Число разбиения интервала интегрирования:', result[1])


finish()