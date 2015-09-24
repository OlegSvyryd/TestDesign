package com.animals.app.domain;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.validation.constraints.DecimalMin;

/**
 * Created by oleg on 22.07.2015.
 */
public class UserRole implements Serializable, TypeHandler<List<? extends UserRole>> {
	
	@DecimalMin(value = "1")
    private Integer id;
	
	@Length(max = 19, message = "The UserRole length must be less than {max}.")
    private String role;

    public UserRole() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        if (!id.equals(userRole.id)) return false;
        if (!role.equals(userRole.role)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;	
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "role='" + role + '\'' +
                ", id=" + id +
                '}';
    }

    /**
     * Override from TypeHandler interface.
     * #setParametr method set parameter data when executing query
     * #getResult methods getting result from query by name column, index column and return value from stored procedure.
     * @param preparedStatement Pre-pared format of query
     * @param i Index column
     * @param userRoles List of values
     * @param jdbcType Type of jdbc data for inserting in database
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<? extends UserRole> userRoles, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(i, userRoles.get(0).getId());
    }

    @Override
    public List<? extends UserRole> getResult(ResultSet resultSet, String s) throws SQLException {
        return (List<? extends UserRole>) resultSet.getObject(s);
    }

    @Override
    public List<? extends UserRole> getResult(ResultSet resultSet, int i) throws SQLException {
        return (List<? extends UserRole>) resultSet.getObject(i);
    }

    @Override
    public List<? extends UserRole> getResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
