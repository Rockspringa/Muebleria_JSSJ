package edu.errores;

public class NonAlfanumericStringException extends RuntimeException {
    
    @Override
    public String getMessage() {
        return "El texto ingresado no era alfanumerico, revise los datos ingresados.";
    }
}
