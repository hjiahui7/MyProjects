package Services;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

public interface ServiceInterface
{
    public String makeWeb(HttpServletRequest req) throws IOException, SQLException;
}
