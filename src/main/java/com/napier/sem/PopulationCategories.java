package com.napier.sem;

public class PopulationCategories {
    private String worldPop;
    private int areaTypeCount;

    public PopulationCategories(String worldPop, int areaTypeCount)
    {
        this.worldPop = worldPop;
        this.areaTypeCount = areaTypeCount;
    }

    public String GetWorldPop()
    {
        return worldPop;
    }

    public int GetCount()
    {
        return areaTypeCount;
    }

    public void SetCount(int i)
    {
        areaTypeCount = i;
    }

    public void SetWorldPop(String n)
    {
        worldPop = n;
    }

}
