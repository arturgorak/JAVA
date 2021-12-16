// 1. 255, jak przekroczy limit to się nie skompiluje
// 2. Aby utworzyc instancje statycznej klasy wewnetrzniej nie potrzebujemy instancji klasy zewnetrznej
// 3. Rozwiązać ten problem możemy używając słowa super z adnotacją z której klasy dana metoda ma być dziedziczona

package uj.java.pwj2020.spreadsheet;

public class DiamondProblem {
    interface A {
        default void foo() {
            System.out.println("A");
        }
    }

    interface B extends A {
            default void foo() {
                A.super.foo();
                //System.out.println("B");
            }
    }

    interface C extends  A{
        default void foo() {
            System.out.println("C");
        }
    }

    static class D implements B, C{
        public void foo(){
//            C.super.foo();
              B.super.foo();
        }
    }

    static class OuterClass{
        class InnerClass{}
        static class InnerStaticClass{}
    }

    public static void main(String[] args) {
        D tmp = new D();
        tmp.foo();

        OuterClass.InnerStaticClass tmp2 = new OuterClass.InnerStaticClass();
//        OuterClass.InnerClass tmp3 = new OuterClass.InnerClass();

    }
}
