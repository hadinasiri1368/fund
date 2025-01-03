package org.fund.common;

import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CommonUtils {
    public static boolean isNull(Object o) {
        if (o instanceof String) {
            if (o == null ||
                    ((String) o).isEmpty() ||
                    ((String) o).isBlank() ||
                    ((String) o).length() == 0 ||
                    ((String) o).toLowerCase().trim().equals("null")
            )
                return true;
            return false;
        } else if (o instanceof List) {
            if (((List) o).isEmpty())
                return true;
        }
        return o == null ? true : false;
    }

    public static boolean executeQuery(DataSource dataSource, String query) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String loadSqlFromFile(String path) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(path);
        return new String(Files.readAllBytes(Paths.get(resource.getURI())), StandardCharsets.UTF_8);
    }

    public static void setNull(Object entity) throws Exception {
        if (isNull(entity)) {
            throw new FundException(GeneralExceptionType.ENTITY_CANNOT_BE_NULL);
        }
        // get Class and fields
        Class<?> cls = entity.getClass();
        Field[] fields = cls.getDeclaredFields();

        for (Field field : fields) {
            // access to private field
            field.setAccessible(true);

            // create getter method with field name
            String capitalizedFieldName = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
            String getterMethodName = "get" + capitalizedFieldName;
            Method getterMethod = cls.getDeclaredMethod(getterMethodName);

            //  get value from getter
            Object fieldValue = getterMethod.invoke(entity);

            if (isNull(fieldValue)) {
                String setterMethodName = "set" + capitalizedFieldName;
                Method setterMethod = cls.getDeclaredMethod(setterMethodName, field.getType());
                setterMethod.invoke(entity, (Object) null);
            }
        }
    }

    private static long get64LeastSignificantBitsForVersion1() {
        Random random = new Random();
        long random63BitLong = random.nextLong() & 0x3FFFFFFFFFFFFFFFL;
        long variant3BitFlag = 0x8000000000000000L;
        return random63BitLong | variant3BitFlag;
    }

    private static long get64MostSignificantBitsForVersion1() {
        final long currentTimeMillis = System.currentTimeMillis();
        final long time_low = (currentTimeMillis & 0x0000_0000_FFFF_FFFFL) << 32;
        final long time_mid = ((currentTimeMillis >> 32) & 0xFFFF) << 16;
        final long version = 1 << 12;
        final long time_hi = ((currentTimeMillis >> 48) & 0x0FFF);
        return time_low | time_mid | version | time_hi;
    }

    public static UUID generateUUID() {
        long most64SigBits = get64MostSignificantBitsForVersion1();
        long least64SigBits = get64LeastSignificantBitsForVersion1();
        return new UUID(most64SigBits, least64SigBits);
    }
}
