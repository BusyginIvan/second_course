import os
import math
import numpy as np
import matplotlib.pyplot as plt


def finish():
  print('\nПрограмма завершила работу.\n')
  os.system('pause')
  exit()


print("Это программа для решения дифференциального уравнения y' = x + y точными и приближёнными методами.")

a = input('\nВведите координату x левой границы интервала дифференцирования: ')
try:
  a = float(a)
except Exception:
  print('Ошибка! Надо было ввести число, а вы ввели какую-то ерунду.')
  finish()

b = input('\nВведите координату x правой границы интервала дифференцирования: ')
try:
  b = float(b)
except Exception:
  print('Ошибка! Надо было ввести число, а вы ввели какую-то ерунду.')
  finish()
if b <= a:
  print('Ошибка! Координата x правой границы должна быть больше, чем у левой.')
  finish()

y0 = input('\nВведите значение интеграла дифференциального уравнения в левой границе интервала дифференцирования: ')
try:
  y0 = float(y0)
except Exception:
  print('Ошибка! Надо было ввести число, а вы ввели какую-то ерунду.')
  finish()

n = input('\nВведите число интервалов разбиения (больше трёх): ')
try:
  n = int(n)
except Exception:
  print('Ошибка! Надо было ввести натуральное число, а вы ввели какую-то ерунду.')
  finish()
if n <= 3:
  print('Ошибка! Ожидалось натуральное число больше трёх.')
  finish()

accuracy = input('\nЗадайте точность вычислений приближёнными методами (положительное число): ')
try:
  accuracy = float(accuracy)
except Exception:
  print('Ошибка! Надо было ввести число, а вы ввели какую-то ерунду.')
  finish()
if accuracy <= 0:
  print('Ошибка! Ожидалось положительное число.')
  finish()


f = lambda x, y: x + y
h = (b - a) / (n - 1)
X = np.linspace(a, b, n)


C = (y0 + a + 1) / math.exp(a)
exactSolution = lambda x: C * math.exp(x) - x - 1
exactSolutionY = [exactSolution(x) for x in X]
exactSolutionDetailedX = np.linspace(a, b, 200)
exactSolutionDetailedY = [exactSolution(x) for x in exactSolutionDetailedX]


def approximateSolution(nextY, degreeOfAccuracy):
  Y = [y0]; detailedX = [a]; detailedY = [y0]
  for i in range(n - 1):
    subN = 2
    oldY = nextY(X[i], Y[i], h)
    while True:
      subH = h / subN
      subX = [X[i]]; subY = [Y[i]]
      for j in range(subN):
        subX.append(subX[-1] + subH)
        subY.append(nextY(subX[-1], subY[-1], subH))
      newY = subY[-1]
      if abs(newY - oldY) / (2**degreeOfAccuracy - 1) < accuracy: break
      oldY = newY; subN *= 2
    detailedX.extend(subX[1:])
    detailedY.extend(subY[1:])
    Y.append(newY)
  return (Y, detailedX, detailedY)

eulerNextY = lambda x, y, h: y + h / 2 * (f(x, y) + f(x + h, y + h * f(x, y)))
eulerY, eulerDetailedX, eulerDetailedY = approximateSolution(eulerNextY, 2)

def rungeNextY(x, y, h):
  k1 = f(x, y); k2 = f(x + h/2, y + h/2 * k1); k3 = f(x + h/2, y + h/2 * k2); k4 = f(x + h, y + h * k3)
  return y + h/6 * (k1 + 2 * k2 + 2 * k3 + k4)

rungeY, rungeDetailedX, rungeDetailedY = approximateSolution(rungeNextY, 4)


#     ' .....  ..............  ..............  .................   ..............'
print('   i          x          Метод Эйлера   Метод Рунге-Кутта   Точное решение')
for i in range(n):
  print(' %5d  %13.5f   %13.5f   %15.5f    %13.5f' % (i, X[i], eulerY[i], rungeY[i], exactSolutionY[i]))
print('\n')


_, ax = plt.subplots(figsize=(8, 5), label = 'Моё окно')

ax.plot(eulerDetailedX        , eulerDetailedY        , '-' , label = 'метод Эйлера'     , alpha = 0.7, color = 'red'  )
ax.plot(rungeDetailedX        , rungeDetailedY        , '-' , label = 'метод Рунге-Кутта', alpha = 0.7, color = 'blue' )
ax.plot(exactSolutionDetailedX, exactSolutionDetailedY, '--', label = 'точное решение'   , alpha = 1.0, color = 'black')

for i in range(n):
  ax.plot(X[i], eulerY[i]        , marker = 'o', fillstyle = 'full', alpha = 0.8, markerfacecolor = 'red'  , markeredgecolor = 'red'  )
  ax.plot(X[i], rungeY[i]        , marker = 'o', fillstyle = 'full', alpha = 0.8, markerfacecolor = 'blue' , markeredgecolor = 'blue' )
  ax.plot(X[i], exactSolutionY[i], marker = 'o', fillstyle = 'full', alpha = 0.8, markerfacecolor = 'white', markeredgecolor = 'black')

ax.grid(True)
ax.set_title('Прекрасный график')
plt.legend()
plt.show()


finish()