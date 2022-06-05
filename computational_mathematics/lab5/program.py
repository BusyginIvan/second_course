import os
import numpy as np
import math
import sys
import matplotlib.pyplot as plt


def finish():
  print('\nПрограмма завершила работу.\n')
  os.system('pause')
  exit()


choice = input("Желаете ли вы задать узлы интерполяции равноотстоящими друг от друга? Если да, введите 'y': ")
if choice == 'y':
  equidistant = True
  
  a = input('\nВведите координату x самого левого узла интерполяции: ')
  try:
    a = float(a)
  except Exception:
    print('Ошибка! Надо было ввести число, а вы ввели какую-то ерунду.')
    finish()
  
  b = input('\nВведите координату x самого правого узла интерполяции: ')
  try:
    b = float(b)
  except Exception:
    print('Ошибка! Надо было ввести число, а вы ввели какую-то ерунду.')
    finish()

  n = input('\nВведите число интервалов разбиения (больше одного): ')
  try:
    n = int(n)
  except Exception:
    print('Ошибка! Надо было ввести натуральное число, а вы ввели какую-то ерунду.')
    finish()
  if n < 2:
    print('Ошибка! Ожидалось натуральное число больше единицы.')
    finish()

  X = np.linspace(a, b, n + 1)
else:
  print('\nВведите через пробел координату x узлов интерполяции:')
  X = input().split(' ')
  try:
    X = [float(x) for x in X]
  except Exception:
    print('Ошибка! Надо было ввести числа, а вы ввели какую-то ерунду.')
    finish()
  if len(X) == 0:
    print('Ожидалось, что вы введёте хотя бы одно значение.')
    finish()
  
  n = len(X) - 1
  
  if n >= 2:
    equidistant = True
    dist = X[1] - X[0]
    for i in range(1, n):
      if X[i + 1] - X[i] != dist:
        equidistant = False
        break
  else:
    equidistant = False


print('\nВыберите способ получения координаты y для узлов интерполяции:')
print('1. Ввести вручную.')
print('2. Выбрать функцию для автоматического расчёта.')

num = input('Введите номер выбранного варианта: ')
try:
  num = int(num)
except Exception:
  print('Ошибка! Надо было ввести число, а вы ввели какую-то ерунду.')
  finish()

if num == 1:
  useFunc = False

  print('\nВведите через пробел координату y узлов интерполяции:')
  Y = input().split(' ')
  
  try:
    Y = [float(y) for y in Y]
  except Exception:
    print('Ошибка! Надо было ввести числа, а вы ввели какую-то ерунду.')
    finish()
  
  if len(Y) <= n:
    print('Ошибка! Вы ввели меньше значений, чем было для координаты x.')
    finish()
  
  Y = Y[:n + 1]
elif num == 2:
  useFunc = True

  print('\nВыберите функцию для вычисления координаты y:')
  print('1. sin(x)')
  print('2. exp(x)')
  
  num = input('Введите номер выбранного варианта: ')
  try:
    num = int(num)
  except Exception:
    print('Ошибка! Надо было ввести число, а вы ввели какую-то ерунду.')
    finish()
  
  if num == 1:
    func = lambda x: math.sin(x)
  elif num == 2:
    func = lambda x: math.exp(x)
  else:
    print('Ошибка! Ожидалось 1 или 2, а вы чего ввели?')
    finish()
  
  Y = [func(x) for x in X]
else:
  print('Ошибка! Ожидалось 1 или 2, а вы чего ввели?')
  finish()


x = input('\nВведите координату x точки интерполирования: ')
try:
  x = float(x)
except Exception:
  print('Ошибка! Надо было ввести число, а вы ввели какую-то ерунду.')
  finish()


for i in range(n + 1):
  if x == X[i]:
    print('Значение x совпало с координатой одного из узлов интерполяции. Значение функции в этой точке:', Y[i])
    finish()


def lagrange(x, X, Y, n):
  sum = 0
  for i in range(n + 1):
    mul = Y[i]
    for j in range(n + 1):
      if j == i: continue
      mul *= (x - X[j]) / (X[i] - X[j])
    sum += mul
  return sum

def gauss1(x, X, DY, h, middle):
  t = (x - X[middle]) / h
  sum = DY[0][middle] + t * DY[1][middle]
  mul = t
  for i in range(1, middle):
    mul *= (t - i) / (2 * i)
    sum += mul * DY[2 * i][middle - i]
    mul *= (t + i) / (2 * i + 1)
    sum += mul * DY[2 * i + 1][middle - i]
  mul *= (t - middle) / (2 * middle)
  sum += mul * DY[2 * middle][0]
  return sum

def gauss2(x, X, DY, h, middle):
  t = (x - X[middle]) / h
  sum = DY[0][middle]
  mul = 1
  for i in range(1, middle + 1):
    mul *= (t - i + 1) / (2 * i - 1)
    sum += mul * DY[2 * i - 1][middle - i]
    mul *= (t + i) / (2 * i)
    sum += mul * DY[2 * i][middle - i]
  return sum


lFunc = lambda x: lagrange(x, X, Y, n)


if equidistant:
  if x < X[0]:
    print('\nДля интерполяции будет применён только метод Лагранжа, так как точка интерполирования лежит левее всех узлов интерполирования.')
    useGauss = False
  elif x > X[n]:
    print('\nДля интерполяции будет применён только метод Лагранжа, так как точка интерполирования лежит правее всех узлов интерполирования.')
    useGauss = False
  else:
    useGauss = True
else:
  print('\nПри неравных интервалах между узлами интерполяции эта программа применяет для интерполяции только метод Лагранжа.')
  useGauss = False

if useGauss:
  h = (X[n] - X[0]) / n
  l = math.floor((x - X[0]) / h)
  middle = n//2
  
  if n % 2 == 1 and l == middle:
    gN = n - 1
    if x < (X[0] + X[n]) / 2:
      gX = X[:-1]
      gDY = [Y[:-1]]
    else:
      gX = X[1:]
      gDY = [Y[1:]]
  elif l < middle:
    gN = (l + 1) * 2
    gX = X[:gN + 1]
    gDY = [Y[:gN + 1]]
  else:
    gN = (n - l) * 2
    gX = X[n - gN:]
    gDY = [Y[n - gN:]]
  
  for i in range(gN):
    gDY.append([gDY[i][j + 1] - gDY[i][j] for j in range(gN - i)])
  
  gMiddle = gN//2
  
  if x < gX[gMiddle]:
    gFunc = lambda x: gauss2(x, gX, gDY, h, gMiddle)
  else:
    gFunc = lambda x: gauss1(x, gX, gDY, h, gMiddle)


print('\nРезультат интерполяции многочленом Лагранжа:', lFunc(x))
if useGauss:
  print('Результат интерполяции многочленом Гаусса:', gFunc(x))


indent = (X[-1] - X[0]) / 10

pX = np.linspace(X[0] - indent, X[-1] + indent, 100)
pY = [lFunc(x) for x in pX]
plt.plot(pX, pY, '-r', label = 'многочлен Лагранжа', alpha = 0.7, zorder = 1)
plt.scatter(x, lFunc(x), color = 'red', alpha = 0.7, zorder = 1)

if useGauss:
  pY = [gFunc(x) for x in pX]
  plt.plot(pX, pY, '-b', label = 'многочлен Гаусса', alpha = 0.7, zorder = 2)
  plt.scatter(x, gFunc(x), color = 'blue', alpha = 0.7, zorder = 2)

if useFunc:
  pY = [func(x) for x in pX]
  plt.plot(pX, pY, '--k', label = 'интерполируемая функция', alpha = 0.8, zorder = 3)
for i in range(n + 1):
  plt.scatter(X[i], Y[i], color = 'black', alpha = 0.8, zorder = 3)

plt.grid(True)
plt.title('Прекрасный график')
plt.legend()
plt.show()