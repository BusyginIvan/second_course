import os
import numpy as np
from mpl_toolkits.mplot3d import Axes3D
import matplotlib.pyplot as plt
from matplotlib import cm
from matplotlib.ticker import LinearLocator, FormatStrFormatter


def finish():
  print('Программа завершила работу.\n')
  os.system("pause")
  exit()


print('Это программа для нахождения корней системы двух уравнений:')
print(' 1.8x³ - 2.47y² - 5.53x + 1.539 = 0')
print(' 2x³ + 3.41x² - 23.74y + 2.95 = 0\n')

try:
  admissibleError = float(input('Введите точность (погрешность) вычислений: '))
except ValueError:
  print('Ошибка! В качестве точности должно быть указано число.')
  finish()
if admissibleError <= 0:
  print('Ошибка! Точность должна быть положительным числом.')
  finish()

firstPoint = input('Введите через запятую две координаты начальной точки: ').split(',')
try:
  firstPoint = np.array((float(firstPoint[0]), float(firstPoint[1])))
except ValueError:
  print('Ошибка! Координатами должны быть числа (разделиель целой и дробной части – точка).')
  finish()


f = lambda point: 1.8 * point[0]**3 - 2.47 * point[1]**2 - 5.53 * point[0] + 1.539
fDerX = lambda point: 5.4 * point[0]**2 - 5.53
fDerY = lambda point: - 4.94 * point[1]

g = lambda point: 2 * point[0]**3 + 3.41 * point[0]**2 - 23.74 * point[1] + 2.95
gDerX = lambda point: 6 * point[0]**2 + 6.82 * point[0]
gDerY = lambda point: -23.74

#f = lambda point: point[0]**2 + point[1]**2 - 4
#fDerX = lambda point: 2 * point[0]
#fDerY = lambda point: 2 * point[1]
#
#g = lambda point: point[1] - 3 * point[0]**2
#gDerX = lambda point: -6 * point[0]
#gDerY = lambda point: 1


def solveSystem(point, admissibleError, f, fDerX, fDerY, g, gDerX, gDerY):
  if f(point) == 0 and g(point) == 0:
    return (0, point, (0, 0))
  iter = 1
  while True:
    matrix = np.array(((fDerX(point), fDerY(point)), (gDerX(point), gDerY(point))))
    if np.linalg.det(matrix) == 0:
      print('Ошибка! Не удалось вычислить очередное приближение корня из-за его бесконечности или неоднозначности.')
      finish()
    changesVec = -np.linalg.inv(matrix) @ np.array((f(point), g(point)))
    point += changesVec
    changesVec = np.abs(changesVec)
    if np.max(changesVec) < admissibleError:
      return (iter, point, changesVec)
    iter += 1


result = solveSystem(firstPoint, admissibleError, f, fDerX, fDerY, g, gDerX, gDerY)
print('\nНайденный корень:', result[1])
print('Вектор величин изменений на последней итерации:', result[2])
print('Число итераций:', result[0], '\n')


x, y = result[1]
indent = 5
n = 10

X = np.linspace(x - indent, x + indent, n)
Y = np.linspace(x - indent, x + indent, n)
X, Y = np.meshgrid(X, Y)

F = 1.8 * X**3 - 2.47 * Y**2 - 5.53 * X + 1.539
G = 2 * X**3 + 3.41 * X**2 - 23.74 * Y + 2.95

fig = plt.figure()
ax = fig.gca(projection='3d')

ax.scatter(x, y, 0, c = (0, 0, 0), zorder = 2)
ax.plot_surface(X, Y, F, alpha=0.8, zorder = 1)
ax.plot_surface(X, Y, G, alpha=0.8, zorder = 1)

max = max(np.max(F), np.max(G))
min = min(np.min(F), np.min(G))
ax.set_zlim(min * 0.8, max * 0.8)
ax.zaxis.set_major_locator(LinearLocator(8))
ax.zaxis.set_major_formatter(FormatStrFormatter('%.02f'))

plt.show()


finish()