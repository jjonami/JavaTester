package kr.co.sampler.crawling.common.model.enums;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * https://www.holaxprogramming.com/2015/11/12/spring-boot-mybatis-typehandler/ 참조
 * @param <E>
 * enum String -> int
 */
public abstract class AbstractEnumConverter<E extends Enum<E>> implements TypeHandler<CodeEnumValue> {
    private final Class<E> enumType;

    public AbstractEnumConverter(Class<E> enumType) {
        this.enumType = enumType;
    }


    @Override
    public void setParameter(PreparedStatement ps, int i, CodeEnumValue parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i,parameter.getValue());
    }

    @Override
    public CodeEnumValue getResult(ResultSet rs, String columnName) throws SQLException {
        int code = rs.getInt(columnName);
        return getCodeEnum(code);
    }

    @Override
    public CodeEnumValue getResult(ResultSet rs, int columnIndex) throws SQLException {
        int code = rs.getInt(columnIndex);
        return getCodeEnum(code);
    }

    @Override
    public CodeEnumValue getResult(CallableStatement cs, int columnIndex) throws SQLException {
        int code = cs.getInt(columnIndex);
        return getCodeEnum(code);
    }
    private CodeEnumValue getCodeEnum(int code) {
        try {
            CodeEnumValue[] enumConstants = (CodeEnumValue[]) enumType.getEnumConstants();
            for (CodeEnumValue codeNum: enumConstants) {
                if (codeNum.getValue().equals(code)) {
                    return codeNum;
                }
            }
            return null;
        } catch (Exception e) {
            throw new TypeException("Can't make enum object '" + enumType + "'", e);
        }
    }
}
