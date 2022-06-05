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


nToH = lambda n: (b - a) / (n - 1)


C = (y0 + a + 1) / math.exp(a)
exactSolution = lambda x: C * math.exp(x) - x - 1

exactSolutionN = 200
exactSolutionX = np.linspace(a, b, exactSolutionN)
exactSolutionY = [exactSolution(x) for x in exactSolutionX]


eulerNextY = lambda x, y, h: y + h / 2 * (f(x, y) + f(x + h, y + h * f(x, y)))

eulerN = 3; eulerH = nToH(eulerN)
eulerDegreeOfAccuracy = 2
while abs(eulerNextY(a + eulerH, eulerNextY(a, y0, eulerH), eulerH) - eulerNextY(a, y0, 2 * eulerH)) / (2**eulerDegreeOfAccuracy - 1) > accuracy:
  eulerN *= 2; eulerH = nToH(eulerN)
print('\n\nДля достижения заданной точности в методе Эйлера будет использоваться интервал разбиения', eulerH, '\n')

eulerX = np.linspace(a, b, eulerN)
eulerY = [y0]
for i in range(1, eulerN):
  eulerY.append(eulerNextY(eulerX[i - 1], eulerY[i - 1], eulerH))

exactSolutionYForEulerX = [exactSolution(x) for x in eulerX]
print('   i          x          Метод Эйлера   Точное решение')
for i in range(eulerN):
  print(' %5d  %14.5f  %14.5f  %14.5f ' % (i, eulerX[i], eulerY[i], exactSolutionYForEulerX[i]))
print('\n')


def adamsNextY(x0, y0, y1, y2, y3, h):
  f0 = f(x0, y0); f1 = f(x0 + h, y1); f2 = f(x0 + 2 * h, y2); f3 = f(x0 + 3 * h, y3)
  return y3 + h / 24 * (-9 * f0 + 37 * f1 - 59 * f2 + 55 * f3)

adamsN = 5; adamsH = nToH(adamsN)
adamsDegreeOfAccuracy = 4
while abs(eulerNextY(a + adamsH, eulerNextY(a, y0, adamsH), adamsH) - eulerNextY(a, y0, 2 * adamsH)) / (2**adamsDegreeOfAccuracy - 1) > accuracy:
  adamsN *= 2; adamsH = nToH(adamsN)
if adamsN == 5:
  print('Метод Адамса имеет смысл при четырёх интервалах в разбиении и более, в данном случае нужная точность достигается уже при четырёх интервалах, так что один интервал разбиения будет равен', adamsH)
else:
  print('В методе Адамса будет использоваться интервал разбиения', adamsH)
print()

adamsX = np.linspace(a, b, adamsN)
adamsY = [y0]
for i in range(1, 4):
  adamsY.append(eulerNextY(adamsX[i - 1], adamsY[i - 1], adamsH))
for i in range(4, adamsN):
  adamsY.append(adamsNextY(adamsX[i - 4], adamsY[i - 4], adamsY[i - 3], adamsY[i - 2], adamsY[i - 1], adamsH))

exactSolutionYForAdamsX = [exactSolution(x) for x in adamsX]
print('   i          x          Метод Адамса   Точное решение')
for i in range(adamsN):
  print(' %5d  %14.5f  %14.5f  %14.5f ' % (i, adamsX[i], adamsY[i], exactSolutionYForAdamsX[i]))


_, ax = plt.subplots(figsize=(8, 5), label = 'Моё окно')

ax.plot(exactSolutionX, exactSolutionY, '--', label = 'точное решение', alpha = 0.8, zorder = 2, color = 'black')
ax.plot(eulerX        , eulerY        , '-' , label = 'метод Эйлера'  , alpha = 0.8, zorder = 1, color = 'red'  )
ax.plot(adamsX        , adamsY        , '-' , label = 'метод Адамса'  , alpha = 0.8, zorder = 1, color = 'blue' )

ax.grid(True)
ax.set_title('Прекрасный график')
plt.legend()
plt.show()


finish()