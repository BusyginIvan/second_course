import os
import numpy as np
import math
import matplotlib.pyplot as plt
import sys


def finish():
  print('\nПрограмма завершила работу.\n')
  os.system('pause')
  exit()


useFile = input("Желаете ли вы использовать файл с входными данными? Если да, введите 'y': ")
if useFile == 'y':
  path = input('Введите путь к файлу: ')
  
  try:
    file = open(path, 'r')
  except FileNotFoundError:
    print('Ошибка! Такого файла не существует.')
    finish()
  lines = file.readlines()
  file.close()
  
  lines = [line.split(' ') for line in lines]
  try:
    X = [float(line[0]) for line in lines]
    Y = [float(line[1]) for line in lines]
  except Exception:
    print('Ошибка в содержимом файла. Ожидается, что там будут только строки, в каждой из которых будет значение x, пробел и значение y.')
    finish()
else:
  X = []
  Y = []
  print('\nВведите через пробел на отдельных строках значения x и y, а затем пустую строку:')
  while True:
    line = input()
    if line == '': break
    line = line.split(' ')
    try:
      X.append(float(line[0]))
      Y.append(float(line[1]))
    except Exception:
      print('Ошибка! Вы чего-то не то ввели.')
      finish()

X = np.array(X)
Y = np.array(Y)
n = len(X)

if n < 3:
  print('Ошибка! Вы задали слишком мало точек. Ожидались данные о хотя бы трёх точках.')
  finish()

if np.any(X <= 0):
  print('Ошибка! Значения x должны быть положительными.')
  finish()

if np.any(Y <= 0):
  print('Ошибка! Значения y должны быть положительными.')
  finish()


SX = np.array([n, X.sum()])
Xi = X.copy()
for i in range(2, 7):
  Xi *= X
  SX = np.append(SX, Xi.sum())

SXY = [Y.sum()]
XYi = Y.copy()
for i in range(1, 4):
  XYi *= X
  SXY.append(XYi.sum())


useFile = input("Желаете ли вы использовать файл для вывода данных? Если да, введите 'y': ")
if useFile == 'y':
  path = input('Введите путь к файлу: ')
  output = open(path, 'w', encoding = 'utf-8')
else:
  output = sys.stdout
  print()


def polynomialApproximation(n, SX, SXY):
  A = np.fromfunction(lambda i, j: SX[(n - j + i).astype(int)], (n + 1, n + 1))
  d = np.array([SXY[i] for i in range(n + 1)])
  return [e for e in np.linalg.solve(A, d)]

def deviation(φ, X, Y):
  D = np.array([φ(x) for x in X]) - Y
  return ((D * D).sum() / n)**0.5

def format1(x):
  return str(round(x, 4))

def format2(x):
  x = round(x, 4)
  return '+ ' + str(x) if x >= 0 else '- ' + str(-x)


# Линейная аппроксимация
(a1, b1) = polynomialApproximation(1, SX, SXY)
φ1 = lambda x: a1 * x + b1
δ1 = deviation(φ1, X, Y)
label1 = 'φ₁(x) = ' + format1(a1) + ' ∙ x ' + format2(b1)

normX = X - X.sum() / n
normY = Y - Y.sum() / n
r = (normX * normY).sum() / ((normX * normX).sum() * (normY * normY).sum())**0.5

print('Линейная аппроксимация:', file = output)
print(' ' + label1, file = output)
print(' δ₁ =', δ1, file = output)
print(' r =', r, file = output)

best_φ_label = label1
best_φ = φ1
min_δ = δ1

# Квадратичная аппроксимация
(a2, b2, c2) = polynomialApproximation(2, SX, SXY)
φ2 = lambda x: a2 * x**2 + b2 * x + c2
δ2 = deviation(φ2, X, Y)
label2 = 'φ₂(x) = ' + format1(a2) + ' ∙ x² ' + format2(b2) + ' ∙ x ' + format2(c2)

print('\nКвадратичная аппроксимация:', file = output)
print(' ' + label2, file = output)
print(' δ₂ =', δ2, file = output)

if δ2 < min_δ:
  best_φ_label = label2
  best_φ = φ2
  min_δ = δ2

# Кубическая аппроксимация
(a3, b3, c3, d3) = polynomialApproximation(3, SX, SXY)
φ3 = lambda x: a3 * x**3 + b3 * x**2 + c3 * x + d3
δ3 = deviation(φ3, X, Y)
label3 = 'φ₃(x) = ' + format1(a3) + ' ∙ x³ ' + format2(b3) + ' ∙ x² ' + format2(c3) + ' ∙ x ' + format2(d3)

print('\nКубическая аппроксимация:', file = output)
print(' ' + label3, file = output)
print(' δ₃ =', δ3, file = output)

if δ3 < min_δ:
  best_φ_label = label3
  best_φ = φ3
  min_δ = δ3

# Аппроксимация степенной функцией
lnX = np.log(X)
lnY = np.log(Y)
SlnX = np.array([n, lnX.sum(), (lnX * lnX).sum()])
SlnXlnY = [lnY.sum(), (lnX * lnY).sum()]

(a4, b4) = polynomialApproximation(1, SlnX, SlnXlnY)
(a4, b4) = (np.exp(b4), a4)
φ4 = lambda x: a4 * x**b4
δ4 = deviation(φ4, X, Y)
label4 = 'φ₄(x) = ' + format1(a4) + ' ∙ x^(' + format1(b4) + ')'

print('\nАппроксимация степенной функцией:', file = output)
print(' ' + label4, file = output)
print(' δ₄ =', δ4, file = output)

if δ4 < min_δ:
  best_φ_label = label4
  best_φ = φ4
  min_δ = δ4

# Аппроксимация показательной функцией
SXlnY = [lnY.sum(), (X * lnY).sum()]

(a5, b5) = polynomialApproximation(1, SX, SXlnY)
(a5, b5) = (np.exp(b5), a5)
φ5 = lambda x: a5 * np.exp(b5 * x)
δ5 = deviation(φ5, X, Y)
label5 = 'φ₅(x) = ' + format1(a5) + ' ∙ exp(' + format1(b5) + ' ∙ x)'

print('\nАппроксимация показательной функцией:', file = output)
print(' ' + label5, file = output)
print(' δ₅ =', δ5, file = output)

if δ5 < min_δ:
  best_φ_label = label5
  best_φ = φ5
  min_δ = δ5

# Аппроксимация логарифмической функцией
SlnXY = [Y.sum(), (lnX * Y).sum()]

(a6, b6) = polynomialApproximation(1, SlnX, SlnXY)
φ6 = lambda x: a6 * np.log(x) + b6
δ6 = deviation(φ6, X, Y)
label6 = 'φ₆(x) = ' + format1(a6) + 'ln(x) ' + format2(b6)

print('\nАппроксимация логарифмической функцией:', file = output)
print(' ' + label6, file = output)
print(' δ₆ =', δ6, file = output)

if δ6 < min_δ:
  best_φ_label = label6
  best_φ = φ6


indent = (X[-1] - X[0]) / 10
x = np.linspace(X[0] - indent, X[-1] + indent, 200)
y = [best_φ(xi) for xi in x]

plt.plot(x, y, label = best_φ_label)
for i in range(n):
  plt.scatter(X[i], Y[i], color = 'black')

plt.grid(True)
plt.title('График наилучшей аппроксимирующей функции.')
plt.legend()
plt.show()


if useFile == 'y':
  output.close()

finish()