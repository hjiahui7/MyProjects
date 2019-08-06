package prj5;

import java.util.ArrayList;

/**
 * Class of person
 * 
 * @author <Jiahui Huang> <hjiahui7>
 * @version 2017/12/05
 */
public class Person
{
    /**
     * field
     */
    private PropertyEnum hobby;
    private PropertyEnum major;
    private PropertyEnum region;
    private ArrayList<String> choice;


    /**
     * Constructor of Person
     * 
     * @param hobby hobby
     * @param major major
     * @param region region
     * @param choices choices
     */
    public Person(
        PropertyEnum hobby,
        PropertyEnum major,
        PropertyEnum region,
        ArrayList<String> choices)
    {
        setHobby(hobby);
        setMajors(major);
        setRegion(region);
        setChoice(choices);
    }


    /**
     * Convert to string.
     * 
     * @return String
     */
    public String toString()
    {
        StringBuilder string = new StringBuilder();
        String hobbythis = String.valueOf(getHobby());
        String majorthis = String.valueOf(getMajor());
        String regionthis = String.valueOf(getRegion());
        string.append("[");
        string.append(hobbythis + ", ");
        string.append(majorthis + ", ");
        string.append(regionthis + ", ");
        for (int i = 0; i < getNumberOfChoice(); i++)
        {
            String temp = choice.get(i);
            if (i == getNumberOfChoice() - 1)
            {
                string.append(temp);
            }
            else
            {
                string.append(temp + ", ");
            }
        }
        string.append("]");
        return string.toString();
    }


    /**
     * Check if it's empty.
     * 
     * @return true
     */
    public boolean isEmpty()
    {
        boolean result = false;
        if (getNumberOfChoice() == 0)
        {
            result = true;
        }
        return result;
    }


    /**
     * Get the number of choices.
     * 
     * @return number
     */
    public int getNumberOfChoice()
    {
        return choice.size();
    }


    /**
     * Get Hobby
     * 
     * @return getHobby()
     */
    public PropertyEnum getHobby()
    {
        return hobby;
    }


    /**
     * Get Major
     * 
     * @return Property
     */
    public PropertyEnum getMajor()
    {
        return major;
    }


    /**
     * Get Region
     * 
     * @return getRegion
     */
    public PropertyEnum getRegion()
    {
        return region;
    }


    /**
     * Get the choice
     * 
     * @return getChoice
     */
    public ArrayList<String> getChoice()
    {
        return choice;
    }


    /**
     * Set the choice
     * 
     * @param choices
     *            choice
     */
    public void setChoice(ArrayList<String> choices)
    {
        this.choice = choices;
    }


    /**
     * Set the hobby
     * 
     * @param hobby
     *            the hoobby
     */
    public void setHobby(PropertyEnum hobby)
    {
        this.hobby = hobby;
    }


    /**
     * Set the region
     * 
     * @param region the resion
     */
    public void setRegion(PropertyEnum region)
    {
        this.region = region;
    }


    /**
     * Set the mojors
     * 
     * @param otherMajor the major WE WILL USE
     */
    public void setMajors(PropertyEnum otherMajor)
    {
        this.major = otherMajor;
    }

}
