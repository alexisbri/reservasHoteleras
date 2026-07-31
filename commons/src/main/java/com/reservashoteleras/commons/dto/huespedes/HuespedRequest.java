package com.reservashoteleras.commons.dto.huespedes;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record HuespedRequest(

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String nombre,

        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
        String apellidoPaterno,

        @NotBlank(message = "El apellido materno es requerido")
        @Size(min = 2, max = 50, message = "El apellido materno debe tener entre 2 y 50 caracteres")
        String apellidoMaterno,

        @NotBlank(message = "El email es requerido")
        @Email(message = "El formato del email no es válido")
        @Size(max = 100, message = "El email debe tener máximo 100 caracteres")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos")
        String telefono,

        @NotBlank(message = "El documento es requerido")
        @Size(max = 30, message = "El documento debe tener máximo 30 caracteres")
        String documento,

        @NotBlank(message = "La nacionalidad es requerida")
        @Size(max = 50, message = "La nacionalidad debe tener máximo 50 caracteres")
        String nacionalidad

) {
}
