package com.example;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Usuario {
    private String nombre;
    private String correo;
    private String contrasena;

    public Usuario(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }

    public String toCSV() {
        return nombre + "," + correo + "," + contrasena;
    }

    public static Usuario fromCSV(String linea) {
        String[] partes = linea.split(",");
        if (partes.length != 3) return null;
        return new Usuario(partes[0], partes[1], partes[2]);
    }
}

class UsuarioService {
    private List<Usuario> usuarios = new ArrayList<>();
    private final String archivo = "usuarios.txt";

    public UsuarioService() {
        cargarUsuarios();
    }

    public boolean registrar(String nombre, String correo, String contrasena) {
        if (correoExiste(correo)) return false;

        Usuario nuevo = new Usuario(nombre, correo, contrasena);
        usuarios.add(nuevo);
        guardarUsuario(nuevo);
        return true;
    }

    public boolean iniciarSesion(String correo, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo) && u.getContrasena().equals(contrasena)) {
                System.out.println("✅ Bienvenido, " + u.getNombre());
                return true;
            }
        }
        return false;
    }

    private boolean correoExiste(String correo) {
        return usuarios.stream().anyMatch(u -> u.getCorreo().equalsIgnoreCase(correo));
    }

    private void guardarUsuario(Usuario usuario) {
        try (FileWriter fw = new FileWriter(archivo, true)) {
            fw.write(usuario.toCSV() + "\n");
        } catch (IOException e) {
            System.out.println("❌ Error al guardar el usuario.");
        }
    }

    private void cargarUsuarios() {
        File file = new File(archivo);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                Usuario u = Usuario.fromCSV(linea);
                if (u != null) usuarios.add(u);
            }
        } catch (IOException e) {
            System.out.println("❌ Error al cargar usuarios.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UsuarioService usuarioService = new UsuarioService();

        String opcion;

        do {
            System.out.println("\n=== Menú ===");
            System.out.println("1. Registrar");
            System.out.println("2. Iniciar sesión");
            System.out.println("3. Salir");
            System.out.print("Opción: ");
            opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Correo: ");
                    String correo = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String contrasena = scanner.nextLine();

                    if (usuarioService.registrar(nombre, correo, contrasena)) {
                        System.out.println("✅ Registro exitoso.");
                    } else {
                        System.out.println("⚠️ El correo ya está registrado.");
                    }
                    break;

                case "2":
                    System.out.print("Correo: ");
                    String loginCorreo = scanner.nextLine();
                    System.out.print("Contraseña: ");
                    String loginContrasena = scanner.nextLine();

                    if (!usuarioService.iniciarSesion(loginCorreo, loginContrasena)) {
                        System.out.println("❌ Datos incorrectos.");
                    }
                    break;

                case "3":
                    System.out.println("👋 Hasta luego.");
                    break;

                default:
                    System.out.println("⚠️ Opción no válida.");
            }

        } while (!opcion.equals("3"));

        scanner.close();
    }
}
