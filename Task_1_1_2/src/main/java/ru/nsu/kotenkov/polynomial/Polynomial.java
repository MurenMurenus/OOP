package ru.nsu.kotenkov.polynomial;

import java.util.Arrays;
import java.util.Objects;


/**
 * Класс многочлена с объемным функционалом взаимодействия между объектами класса и с пользователем.
 *
 * @author Kotenkov-Maksim
 *
 * @version 1.0
 */
public class Polynomial {
    /**
     * Коэффициенты полинома.
     */
    public int[] cfs;

    /**
     * Степень полинома.
     */
    public int degree;

    /**
     * Конструктор для инициализации полинома с заданными коэффициентами.
     *
     * @param parameters - изначальные коэффициенты полинома.
     */
    public Polynomial(int[] parameters) {
        degree = parameters.length;
        cfs = parameters;
    }

    /**
     * Метод вычисления значения полинома для заданного значения x (val).
     *
     * @param val - значение x, для которого вычисляется значение полинома.
     * @return - int.
     */
    public int evaluate(int val) {
        int res = 0;

        for (int i = 0; i < this.degree; i++) {
            res += (int) (this.cfs[i] * Math.pow(val, i));
        }

        return res;
    }

    /**
     * Метод вычисления производной заданного порядка.
     *
     * @param diff - порядок производной.
     * @return - объект класса Polynomial, у которого был вызван метод.
     */
    public Polynomial differentiate(int diff) {
        for (int d = diff; d > 0; d--) {
            for (int i = 0; i < this.degree - 1; i++) {
                this.cfs[i] = (i + 1) * this.cfs[i + 1];
            }

            if (this.degree <= 0) {
                return this;
            }
            this.degree--;
            this.cfs[this.degree] = 0;

            // int[] n = new int[--this.degree];
            // уйти от ресайзов
            // System.arraycopy(this.cfs, 1, n, 0, this.degree);

            // this.cfs = n;
        }

        return this;
    }

    /**
     *  Метод сравнения двух полиномов.
     *
     * @param o - второй полином, с которым сравнивается this полином.
     * @return - true или false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Polynomial that = (Polynomial) o;
        return degree == that.degree && Arrays.equals(cfs, that.cfs);
    }

    /**
     * Необходимая перезапись хэшкода объектов для @Override сравнения объектов полиномов.
     *
     * @return - int хэшкод.
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(degree);
        result = 31 * result + Arrays.hashCode(cfs);
        return result;
    }

    /**
     * Строковое представление полинома.
     *
     * @return - String.
     */
    @Override
    public String toString() {
        String res = "";

        int pow = this.degree;

        for (int i = this.degree - 1; i >= 0; i--) {
            if (this.cfs[i] != 0) {
                if (pow != this.degree) {
                    if (this.cfs[i] > 0) {
                        res = res.concat(" + ");
                    } else {
                        res = res.concat(" - ");
                    }
                }

                res = res.concat(Integer.toString(Math.abs(this.cfs[i])));
                pow--;
                if (pow > 1) {
                    res = res.concat("x^");
                    res = res.concat(Integer.toString(pow));
                } else if (pow == 1) {
                    res = res.concat("x");
                }
            } else {
                pow--;
            }
        }

        System.out.println(res);
        return res;
    }

    /**
     * Метод сложения двух полиномов.
     *
     * @param p2 - полином, с которым проводится операция.
     * @return - объект Polynomial, от которого был вызван метод.
     */
    public Polynomial plus(Polynomial p2) {
        int[] newCfs;

        if (this.degree < p2.degree) {
            newCfs = p2.cfs;
            for (int i = 0; i < this.degree; i++) {
                newCfs[i] += this.cfs[i];
            }
            this.degree = p2.degree;
        } else {
            newCfs = this.cfs;
            for (int i = 0; i < p2.degree; i++) {
                newCfs[i] += p2.cfs[i];
            }
        }

        this.cfs = newCfs;

        return this;
    }

    /**
     * Метод вычитания полинома-аргумента из объекта.
     *
     * @param p2 - полином, с которым проводится операция.
     * @return - объект Polynomial, от которого был вызван метод.
     */
    public Polynomial minus(Polynomial p2) {
        int[] newCfs;

        if (this.degree < p2.degree) {
            newCfs = p2.cfs;
            for (int i = 0; i < this.degree; i++) {
                newCfs[i] -= this.cfs[i];
            }
            this.degree = p2.degree;
        } else {
            newCfs = this.cfs;
            for (int i = 0; i < p2.degree; i++) {
                newCfs[i] -= p2.cfs[i];
            }
        }

        this.cfs = newCfs;

        return this;
    }

    /**
     * Метод умножения объекта на полином-аргумент.
     *
     * @param p2 - полином, с которым проводится операция.
     * @return - объект Polynomial, от которого был вызван метод.
     */
    public Polynomial times(Polynomial p2) {
        int[] newCfs = new int[(this.degree - 1) + (p2.degree - 1) + 1];

        for (int i = 0; i < this.degree; i++) {
            for (int j = 0; j < p2.degree; j++) {
                newCfs[i + j] += this.cfs[i] * p2.cfs[j];
            }
        }

        this.cfs = newCfs;
        this.degree = this.degree * p2.degree / 2;

        System.out.println(Arrays.toString(this.cfs));

        return this;
    }

    /**
     * Затычка для компиляции...
     *
     * @param args - стандартные параметры main.
     */
    public static void main(String[] args) {
        System.out.println("Compilation completed.");
    }
}
