package org.aguzman.java.jdbc.repositorio;

import java.sql.SQLException;
import java.util.List;
/*
Inteface que me sirve para la creación del CRUD
para cualquier Objeto, simplemente deben implementar esta
interface y darle su respectiva funcinalida.
 */
public interface Repositorio<T> {
    List<T> listar() throws SQLException;

    T porId(Long id) throws SQLException;

    T guardar(T t) throws SQLException;

    void eliminar(Long id) throws SQLException;
}
