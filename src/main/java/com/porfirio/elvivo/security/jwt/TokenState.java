package com.porfirio.elvivo.security.jwt;

public enum TokenState
{
    ACCESS,
    REVOKED,
    EXPIRED
    /*
    REVOKED, //Fue invalidado manualmente (logout, cambio de clave, etc)
    INACTIVE, //Fue invalidado porque no se uso en mucho tiempo
    USED,  //El token ya fue usado y no debe usarse mas
    BLOCKED  //El token pertence a un usuario suspendido o bloqueado
     */
}
