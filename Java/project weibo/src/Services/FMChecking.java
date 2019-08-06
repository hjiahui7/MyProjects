package Services;

public class FMChecking
{

    public static boolean checkName(String name)
    {
        char[] nameArray = name.toCharArray();
        for (char each : nameArray)
        {
            if (Character.isDigit(each))
            {
                return false;
            }
        }
        return true;
    }


    public static boolean checkTime(String time)
    {
        char[] numberArray = time.toCharArray();
        for (char each : numberArray)
        {
            if (Character.isLetter(each))
            {
                return false;
            }
        }
        return true;
    }
}
