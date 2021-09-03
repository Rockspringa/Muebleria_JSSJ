package edu.errores;

public class NegativeNumberException extends RuntimeException {
    
    @Override
    public String getMessage() {
        return "El numero ingresado era negativo. Ingrese un numero positivo";
    }
}
