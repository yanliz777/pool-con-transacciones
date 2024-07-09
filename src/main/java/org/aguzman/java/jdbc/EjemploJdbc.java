package org.aguzman.java.jdbc;

import org.aguzman.java.jdbc.modelo.Categoria;
import org.aguzman.java.jdbc.modelo.Producto;
import org.aguzman.java.jdbc.repositorio.CategoriaRepositorioImpl;
import org.aguzman.java.jdbc.repositorio.ProductoRepositorioImpl;
import org.aguzman.java.jdbc.repositorio.Repositorio;
import org.aguzman.java.jdbc.util.ConexionBaseDatos;

import java.sql.*;
import java.util.Date;
/*
    Ejemplo de transacción con un pool de conexiones, es decir, que todas las operaciones que se le
    hacen a la bd se hacen como si fuera una. En caso de error en una operación
    se ejcutará el roolback, se ejecutan todas o ninguna, así funcionan las transacciones
    Ambos repositorios(categoria y producto) tienen que compartir la misma conexión a la BD
    porque están dentro de la misma transcción
*/
public class EjemploJdbc {
    public static void main(String[] args) throws SQLException {
            /**
             * Se anidan dos try para que no haya error a la hora de cerrar la conexión
             * y así se pueda ejecutar rollback() antes de que se cierre la conexión
              */
            try(Connection conn = ConexionBaseDatos.getConnection()) {
/*
despues de la vadilación lo ponemos falso.En transacciones siempre se hace,
 ya que por defecto viene true,sin
embargo se hace una validación por buenas practicas
 */
                    if (conn.getAutoCommit()) {
                            conn.setAutoCommit(false);
                    }
                    try {
                            //Creamos objeto categoria y le enviamos la coneión por parámetro
                            Repositorio<Categoria> repositorioCategoria = new CategoriaRepositorioImpl(conn);
                            System.out.println("============= Insertar nueva categoria =============");
                            Categoria categoria = new Categoria();
                            categoria.setNombre("Electrohogar");
                            Categoria nuevaCategoria = repositorioCategoria.guardar(categoria);//guardamos categoria
                            System.out.println("Categoria guardada con éxito: " + nuevaCategoria.getId());

                            Repositorio<Producto> repositorio = new ProductoRepositorioImpl(conn);
                            System.out.println("============= listar =============");
                            //repositorio.listar().forEach(System.out::println);

                            for (int i = 0; i < repositorio.listar().size(); i++) {
                                    System.out.println(repositorio.listar().get(i));
                            }

                            System.out.println("============= obtener por id =============");
                            System.out.println(repositorio.porId(1L));

                            System.out.println("============= insertar nuevo producto =============");
                            Producto producto = new Producto();
                            producto.setNombre("Refrigerador Samsung");
                            producto.setPrecio(9900);
                            producto.setFechaRegistro(new Date());
                            producto.setSku("abcdefg123");

                            producto.setCategoria(nuevaCategoria);//guardamos la categoria del producto
                            repositorio.guardar(producto);//guardamos el producto
                            System.out.println("Producto guardado con éxito: " + producto.getId());
                           // repositorio.listar().forEach(System.out::println);
                            for (int i = 0; i < repositorio.listar().size(); i++) {
                                    System.out.println(repositorio.listar().get(i));
                            }
 //Hacemos el commit en caso de que la transacción sea ejecutada con exito
                            conn.commit();

                    } catch (SQLException e) {
                            conn.rollback();
                            e.printStackTrace();
                    }
            }
    }
}
