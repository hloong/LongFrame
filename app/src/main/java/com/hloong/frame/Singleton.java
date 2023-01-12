package com.hloong.frame;

public enum Singleton {
    INSTANCE;
    public void doSomeThing(){
        System.out.println("dosomething+"+this.hashCode());
    }
    public void doOtherThing(){
        System.out.println("doOtherThing+"+this.hashCode());
    }
}
