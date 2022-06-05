import os
import math
import matplotlib.pyplot as plt
import numpy as np

def finish():
  print('Программа завершила работу.\n')
  os.system("pause")
  exit()

useFile = input("Желаете ли вы использовать файл с входными данными? Если да, введите 'y': ")
print()
if useFile == 'y':
  path = input('Введите путь к файлу: ')
  print()
  try:
    file = open(path, 'r')
  except FileNotFoundError:
    print('Ошибка! Такого файла не существует.')
    finish()
  lines = file.readlines()
  file.close()
  
  if len(lines) < 4:
    print('Ошибка! В файле должно быть четыре строки с данными: "admissible error", "left border", "right border" и "first value".')
    finish()
  
  admissibleError = lines[0].split(': ')
  if len(admissibleError) != 2 or admissibleError[0] != 'Admissible error':
    print('Ошибка! В первой строке файла ожидолось "Admissible error: «значение»".')
    finish()
  try:
    admissibleError = float(admissibleError[1])
  except ValueError:
    print('Ошибка! В качестве точности должно быть указано число.')
    finish()
  if admissibleError <= 0:
    print('Ошибка! Точность должна быть положительным числом.')
    finish()
  
  leftBorder = lines[1].split(': ')
  if len(leftBorder) != 2 or leftBorder[0] != 'Left border':
    print('Ошибка! Во второй строке файла ожидолось "Left border: «значение»".')
    finish()
  try:
    leftBorder = float(leftBorder[1])
  except ValueError:
    print('Ошибка! Левой границей интервала, содержащего корень, должно быть число.')
    finish()
  
  rightBorder = lines[2].split(': ')
  if len(rightBorder) != 2 or rightBorder[0] != 'Right border':
    print('Ошибка! В третьей строке файла ожидолось "Right border: «значение»".')
    finish()
  try:
    rightBorder = float(rightBorder[1])
  except ValueError:
    print('Ошибка! Правой границей интервала, содержащего корень, должно быть число.')
    finish()
    
  equationNumber = lines[3].split(': ')
  if len(equationNumber) != 2 or equationNumber[0] != 'Equation number':
    print('Ошибка! В четвёртой строке файла ожидолось "Equation number: «номер уравнения»".')
    finish()
  try:
    equationNumber = int(equationNumber[1])
  except ValueError:
    print('Ошибка! Номером уравнения должно быть натуральное число.')
    finish()
  if equationNumber < 1 or equationNumber > 3:
    print('Ошибка! Номером уравнения должно быть натуральное число от 1 до 3.')
    finish()
else:
  print('Есть три уравнения:')
  print(' 1. cos(x) - x = 0')
  print(' 2. x³ - 3,78x² + 1,25x + 3,49 = 0')
  print(' 3. e^x - x³\n')
  
  try:
    equationNumber = int(input('Введите номер уравнения, корень которого необходимо найти: '))
  except ValueError:
    print('Ошибка! Номером уравнения должно быть натуральное число.')
    finish()
  if equationNumber < 1 or equationNumber > 3:
    print('Ошибка! Номером уравнения должно быть натуральное число от 1 до 3.')
    finish()
  
  try:
    admissibleError = float(input('Введите точность (погрешность) вычислений: '))
  except ValueError:
    print('Ошибка! В качестве точности должно быть указано число.')
    finish()
  if admissibleError <= 0:
    print('Ошибка! Точность должна быть положительным числом.')
    finish()
  
  try:
    leftBorder = float(input('Введите левую границу интервала, содержащего корень: '))
  except ValueError:
    print('Ошибка! Левой границей интервала, содержащего корень, должно быть число.')
    finish()
  
  try:
    rightBorder = float(input('Введите правую границу интервала, содержащего корень: '))
  except ValueError:
    print('Ошибка! Правой границей интервала, содержащего корень, должно быть число.')
    finish()

if equationNumber == 1:
  func = lambda x: math.cos(x) - x
elif equationNumber == 2:
  func = lambda x: x**3 - 3.78 * x**2 + 1.25 * x + 3.49
else:
  func = lambda x: math.exp(x) - x**3


def solveEquation(func, a, b, admissibleError):
  fa = func(a)
  fb = func(b)
  if fa * fb > 0:
    print('На концах отрезака функция имеет одинаковый знак. Поиск корня невозможен. Его и вовсе может не быть.')
    finish()
  if fa == 0:
    return (a, fa, 0)
  if fb == 0:
    return (b, fb, 0)
  
  iters = 1
  
  while True:
    x = (a * fb - b * fa) / (fb - fa)
    fx = func(x)
    if b - a < admissibleError:
      return (x, fx, iters)
    if fx == 0:
      return (x, fx, iters)
    if fa * fx < 0:
      b = x
    else:
      a = x
    iters += 1

print()
result = solveEquation(func, leftBorder, rightBorder, admissibleError)
print('Найденный корень уравнения:', result[0])
print('Значение функции в этой точке:', result[1])
print('Число осуществлённых итераций при поиске корня:', result[2], '\n')

indent = (rightBorder - leftBorder) / 10
x = np.linspace(leftBorder - indent, rightBorder + indent, 1000)
y = [func(xi) for xi in x]
plt.plot(x, y)
plt.scatter(result[0], result[1])
plt.grid(True)
plt.title('График функции и найденный корень.')
plt.show()

finish()