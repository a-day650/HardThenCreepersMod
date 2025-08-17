package com.awa.hardthencreepersmod.thirst;

public interface IThirst {
    int getThirstLevel();
    void setThirstLevel(int level);
    void addThirst(int amount);
}