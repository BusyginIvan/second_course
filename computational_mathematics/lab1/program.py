import os
import numpy as np
import math

def finish():
    print('\nПрограмма завершила работу.\n')
    os.system("pause")
    exit()

def wordЧисло(x):
    if x == 1: return 'числу'
    if x in (2, 3, 4): return 'числа'
    return 'чисел'

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
    
    accuracy = lines[0].split(': ')
    if len(accuracy) != 2 or accuracy[0] != 'Accuracy':
        print('Ошибка! В первой строке файла ожидолось "Accuracy: «значение»".')
        print(accuracy[0])
        print(accuracy[1])
        finish()
    try:
        accuracy = float(accuracy[1])
    except ValueError:
        print('Ошибка! В качестве точности должно быть указано число.')
        finish()
    if accuracy <= 0:
        print('Ошибка! Точность должна быть положительным числом.')
        finish()
    
    equationsAmount = lines[1].split(': ')
    if len(equationsAmount) != 2 or equationsAmount[0] != 'Number of unknowns':
        print('Ошибка! Во второй строке файла ожидолось "Number of unknowns: «значение»".')
        finish()
    try:
        equationsAmount = int(equationsAmount[1])
    except ValueError:
        print('Ошибка! Вместо числа неизвестных в файле какая-то ерунда.')
        finish()
    if equationsAmount <= 0:
        print('Ошибка! Число неизвестных должно быть положительным.')
        finish()
    if equationsAmount > 20:
        print('Ошибка! Число неизвестных должно быть не больше 20-ти.')
        finish() 
    
    if lines[2] != 'Coefficient matrix:\n':
        print('Ошибка! Формат файла подразумевает, что в третьей строке должно быть: "Coefficient matrix:".')
        finish()
    
    try:
        equationsSystem = [[float(i) for i in lines[3 + j].split()] for j in range(equationsAmount)]
    except ValueError:
        print('\nВ файле должны быть указаны коэффициенты через пробел, а получена какая-то ерунда.')
        finish()
    
    for equation in equationsSystem:
        if len(equation) != equationsAmount + 1:
            print('Ошибка! В каждой строке матрицы коэффициентов должно быть по ', equationsAmount + 1, ' ',
                                                                            wordЧисло(equationsAmount + 1), '.', sep='')
            finish()
else:
    try:
        accuracy = float(input('Введите точность (погрешность) вычислений: '))
    except ValueError:
        print('Ошибка! Ожидалось число, а вы ввели какую-то ерунду.')
        finish()
    if accuracy <= 0:
        print('Ошибка! Ожидалось положительное число.')
        finish()
    
    try:
        equationsAmount = int(input('Введите количество неизвестных: '))
    except ValueError:
        print('Ошибка! Ожидалось целое число, а вы ввели какую-то ерунду.')
        finish()
    if equationsAmount <= 0:
        print('Ошибка! Ожидалось целое положительное число.')
        finish()
    if equationsAmount > 20:
        print('Ошибка! Число неизвестных должно быть не больше 20-ти.')
        finish() 
    
    print('Введите через пробел на отдельных строках коэффициенты системы', equationsAmount, 'уравнений:')
    try:
        equationsSystem = [[float(i) for i in input().split()] for j in range(equationsAmount)]
    except ValueError:
        print('\nОжидался ввод чисел, а получена какая-то ерунда.')
        finish()
    print()
    for equation in equationsSystem:
        if len(equation) != equationsAmount + 1:
            print('Ошибка! В каждой строке должно быть по ', equationsAmount + 1, ' ',
                                                                            wordЧисло(equationsAmount + 1), '.', sep='')
            finish()


class EquationsSystem:
    def __init__(self, A, b):
        self._A = A.astype('float64')
        self._b = b.astype('float64')
    
    def moveRow(self, array, indexFrom, indexTo):
        row = array[indexFrom]
        array = np.delete(array, indexFrom, axis = 0)
        return np.insert(array, indexTo, row, axis = 0)
    
    def orderRows(self):
        absA = np.absolute(self._A)
        for j in range(equationsAmount):
            for i in range(equationsAmount - j):
                if absA[j + i][j] >= absA[j + i][:j].sum() + absA[j + i][j + 1:].sum():
                    absA = self.moveRow(absA, j + i, j)
                    self._A = self.moveRow(self._A, j + i, j)
                    self._b = self.moveRow(self._b, j + i, j)
                    break
            else:
                return False
        return True
    
    def solveSystem(self, accuracy):
        C = self._A.copy()
        d = self._b.copy()
        
        for i in range(equationsAmount):
            d[i] /= C[i][i]
            C[i] = C[i] / -C[i][i]
            C[i][i] = 0
        
        oldX = d.copy()
        maxChange = np.inf
        iteration = 1
        
        while maxChange > accuracy:
            newX = C @ oldX + d
            changes = np.absolute(newX - oldX)
            maxChange = changes.max()
            oldX = newX
            iteration += 1
        
        if accuracy < 1:
            newX = np.around(newX, decimals = math.ceil(math.log10(1 / accuracy)))
        
        return (newX, changes, iteration)


equationsSystem = np.array(equationsSystem)
equationsSystem = EquationsSystem(equationsSystem[:, 0:-1], equationsSystem[:, -1])
if equationsSystem.orderRows():
    result = equationsSystem.solveSystem(accuracy)
    print('Вектор неизвестных:', result[0])
    print('Вектор погрешностей:', result[1])
    print('Число итераций:', result[2])
else:
    print('Данную систему невозможно привести к виду, удовлетворяющему условию диагонального преобладания.')

finish()