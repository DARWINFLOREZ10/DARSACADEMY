package com.example;
import java.util.HashMap;  
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        HashMap<String, String> usuarios = new HashMap<>();
        
        Scanner scanner = new Scanner(System.in);

        System.out.println("Registro de Usuario");

        System.out.print("Ingresa un nombre de usuario: ");
        String usuario = scanner.nextLine();

        if (usuarios.containsKey(usuario)) {
            System.out.println("El usuario ya está registrado.");
        } else {
            System.out.print("Ingresa una contraseña: ");
            String contrasena = scanner.nextLine();

            usuarios.put(usuario, contrasena);
            System.out.println("Registro exitoso.");
        }

        System.out.println("\n Usuarios registrados:");
        for (String user : usuarios.keySet()) {
            System.out.println("Usuario: " + user + ", Contraseña: " + usuarios.get(user));
        }

        scanner.close();
    }
}