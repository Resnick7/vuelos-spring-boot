// Variables globales
let currentSection = 'search';
let ciudadesDisponibles = [];
let usuariosDisponibles = [];

// Función para cambiar de sección
function switchSection(sectionName) {
    // Ocultar todas las secciones
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });

    // Remover clase activa de todos los botones
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
    });

    // Mostrar la sección seleccionada
    document.getElementById(sectionName + '-section').classList.add('active');

    // Activar el botón correspondiente
    event.target.classList.add('active');

    currentSection = sectionName;
}

// Función para cargar datos necesarios para reservas
function cargarDatosReserva() {
    console.log('Iniciando carga de datos de reserva...');
    fetch('/vuelos/datos-reserva')
        .then(response => {
            console.log('Respuesta recibida:', response.status, response.statusText);
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        })
        .then(data => {
            console.log('Datos recibidos:', data);
            // Guardar datos en variables globales
            if (data.ciudades) {
                ciudadesDisponibles = data.ciudades;
                console.log('Ciudades cargadas:', ciudadesDisponibles.length);
                actualizarSelectCiudades();
            } else {
                console.warn('No se recibieron ciudades en la respuesta');
                // Si no hay ciudades, cargar opciones predeterminadas
                cargarCiudadesPredeterminadas();
            }

            if (data.usuarios) {
                usuariosDisponibles = data.usuarios;
                console.log('Usuarios cargados:', usuariosDisponibles.length);
            } else {
                console.warn('No se recibieron usuarios en la respuesta');
                // Si no hay usuarios, cargar opciones predeterminadas
                cargarUsuariosPredeterminados();
            }
        })
        .catch(error => {
            console.error('Error al cargar datos para reserva:', error);
            // Cargar datos predeterminados en caso de error
            cargarCiudadesPredeterminadas();
            cargarUsuariosPredeterminados();

            // Mostrar mensaje de error al usuario
            alert('Hubo un problema al cargar los datos. Se han cargado opciones predeterminadas.');
        });
}

// Función para actualizar el select de ciudades
function actualizarSelectCiudades() {
    const selectCiudades = document.getElementById('destination-city');
    const loadingIndicator = document.getElementById('city-loading');

    if (loadingIndicator) {
        loadingIndicator.style.display = 'block';
    }

    // Actualizar la opción por defecto para indicar carga
    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.textContent = 'Selecciona un destino';

    selectCiudades.innerHTML = '';
    selectCiudades.appendChild(defaultOption);
    selectCiudades.disabled = true;

    // Pequeño retraso para mostrar la animación de carga
    setTimeout(() => {
        // Agregar ciudades disponibles
        if (ciudadesDisponibles && ciudadesDisponibles.length > 0) {
            ciudadesDisponibles.forEach(ciudad => {
                if (ciudad && ciudad.nombreCiudad) {
                    const option = document.createElement('option');
                    option.value = ciudad.nombreCiudad.toLowerCase();
                    option.textContent = ciudad.nombreCiudad + (ciudad.pais ? ', ' + ciudad.pais : '');
                    selectCiudades.appendChild(option);
                }
            });
            console.log('Se han cargado ' + ciudadesDisponibles.length + ' ciudades en el select');
        } else {
            console.log('No hay ciudades disponibles para mostrar');
            const noDataOption = document.createElement('option');
            noDataOption.value = '';
            noDataOption.textContent = 'No hay destinos disponibles';
            noDataOption.disabled = true;
            selectCiudades.appendChild(noDataOption);
        }

        selectCiudades.disabled = false;
        if (loadingIndicator) {
            loadingIndicator.style.display = 'none';
        }
    }, 500);
}

// Función para probar la conexión con el servidor
function testServerConnection() {
    console.log('Probando conexión con el servidor...');
    fetch('/vuelos/test-datos')
        .then(response => {
            console.log('Respuesta de prueba recibida:', response.status, response.statusText);
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        })
        .then(data => {
            console.log('Datos de prueba recibidos:', data);
            alert('Conexión exitosa\n\nCiudades: ' + data.numCiudades + '\nUsuarios: ' + data.numUsuarios);
            // Intentar cargar datos nuevamente
            cargarDatosReserva();
        })
        .catch(error => {
            console.error('Error en la prueba de conexión:', error);
            alert('Error al conectar con el servidor: ' + error.message);
        });
}

// Inicialización cuando se carga la página
document.addEventListener('DOMContentLoaded', function() {
    // Configurar fecha mínima como hoy
    document.getElementById('departure-date').min = new Date().toISOString().split('T')[0];

    // Cargar datos necesarios para reservas
    cargarDatosReserva();

    // Crear botón de depuración (solo visible en desarrollo)
    const debugButton = document.createElement('button');
    debugButton.innerText = '🔄 Probar Conexión';
    debugButton.style.position = 'fixed';
    debugButton.style.bottom = '10px';
    debugButton.style.right = '10px';
    debugButton.style.zIndex = '9999';
    debugButton.style.padding = '8px 12px';
    debugButton.style.backgroundColor = '#f8f9fa';
    debugButton.style.border = '1px solid #dee2e6';
    debugButton.style.borderRadius = '4px';
    debugButton.style.cursor = 'pointer';
    debugButton.onclick = testServerConnection;
    document.body.appendChild(debugButton);

    // Manejar formulario de búsqueda
    document.getElementById('flight-search-form').addEventListener('submit', function(e) {
        e.preventDefault();

        const city = document.getElementById('destination-city').value;
        const date = document.getElementById('departure-date').value;

        if (city && date) {
            searchFlights(city, date);
        }
    });

    // Manejar formulario de perfil
    document.getElementById('user-profile-form').addEventListener('submit', function(e) {
        e.preventDefault();

        const formData = {
            firstName: document.getElementById('first-name').value,
            lastName: document.getElementById('last-name').value,
            email: document.getElementById('user-email').value,
            password: document.getElementById('user-password').value
        };

        // Simular guardado
        alert('✅ Perfil actualizado correctamente');
        console.log('Datos del perfil:', formData);
    });
});

// Función para buscar vuelos
function searchFlights(city, date) {
    const resultsSection = document.getElementById('flight-results');
    const container = document.getElementById('flights-container');

    // Mostrar indicador de carga
    container.innerHTML = '<p style="text-align: center; color: #666;">🔍 Buscando vuelos disponibles...</p>';
    resultsSection.classList.add('show');

    // Construir URL con parámetros de búsqueda
    const searchParams = new URLSearchParams();
    if (city) searchParams.append('ciudad', city);
    if (date) searchParams.append('fecha', date);

    const url = `/vuelos/buscar?${searchParams.toString()}`;

    // Realizar la búsqueda en el servidor
    fetch(url)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        })
        .then(vuelos => {
            if (vuelos && vuelos.length > 0) {
                const flights = mapVuelosToFlights(vuelos, city);
                displayFlights(flights);
            } else {
                container.innerHTML = `
                    <div style="background: #f8f9fa; padding: 30px; border-radius: 12px; border: 2px dashed #dee2e6; text-align: center;">
                        <p style="color: #6c757d; font-size: 1.1rem;">✈️ No se encontraron vuelos</p>
                        <p style="color: #6c757d; margin-top: 10px;">Intenta con otra fecha o destino</p>
                    </div>
                `;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            container.innerHTML = `
                <div style="background: #f8d7da; padding: 20px; border-radius: 12px; border: 1px solid #f5c6cb; text-align: center;">
                    <p style="color: #721c24; font-size: 1.1rem;">❌ Error al buscar vuelos</p>
                    <p style="color: #721c24; margin-top: 10px;">${error.message}</p>
                </div>
            `;
        });
}

// Mapear vuelos del servidor al formato de la UI
function mapVuelosToFlights(vuelos, cityFilter) {
    return vuelos.map((vuelo, index) => {
        // Obtener información del destino
        let destination = 'Destino no disponible';
        if (vuelo.aeropuertos && vuelo.aeropuertos.length > 0) {
            const destinationAirport = vuelo.aeropuertos.find(aeropuerto => 
                aeropuerto.ciudad && 
                aeropuerto.ciudad.nombreCiudad && 
                aeropuerto.ciudad.nombreCiudad.toLowerCase().includes(cityFilter.toLowerCase())
            ) || vuelo.aeropuertos[vuelo.aeropuertos.length - 1];

            if (destinationAirport.ciudad) {
                destination = destinationAirport.ciudad.nombreCiudad;
            }
        }

        // Obtener información de la aerolínea
        let airline = 'Aerolínea no disponible';
        if (vuelo.aerolinea && vuelo.aerolinea.nombre) {
            airline = vuelo.aerolinea.nombre;
        }

        // Generar horarios de salida y llegada (simulados)
        const departureHour = 8 + (index * 3);
        const arrivalHour = departureHour + 2;

        // Obtener precio (simulado o desde tarifas si están disponibles)
        let price = '$' + (250 + (index * 50));
        if (vuelo.tarifas && vuelo.tarifas.length > 0) {
            const economyTarifa = vuelo.tarifas.find(tarifa => 
                tarifa.clase && tarifa.clase === 'ECONOMY'
            ) || vuelo.tarifas[0];

            if (economyTarifa.precio) {
                price = '$' + economyTarifa.precio;
            }
        }

        return {
            id: vuelo.id,
            destination: destination,
            departure: `${departureHour.toString().padStart(2, '0')}:00`,
            arrival: `${arrivalHour.toString().padStart(2, '0')}:30`,
            price: price,
            airline: airline
        };
    });
}

// Mostrar vuelos
function displayFlights(flights) {
    const container = document.getElementById('flights-container');
    container.innerHTML = '';

    flights.forEach(flight => {
        const flightCard = document.createElement('div');
        flightCard.className = 'flight-card';
        flightCard.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <div>
                    <h4 style="color: #333; margin-bottom: 5px;">${flight.airline}</h4>
                    <p style="color: #666;">Destino: ${flight.destination}</p>
                    <p style="color: #666;">Salida: ${flight.departure} - Llegada: ${flight.arrival}</p>
                </div>
                <div style="text-align: right;">
                    <div style="font-size: 1.5rem; font-weight: bold; color: #667eea;">${flight.price}</div>
                    <button onclick="reserveFlight(${flight.id})" style="background: #28a745; color: white; border: none; padding: 8px 16px; border-radius: 15px; cursor: pointer; margin-top: 5px; transition: all 0.3s ease;">
                        Reservar
                    </button>
                </div>
            </div>
        `;
        container.appendChild(flightCard);
    });
}

// Función para reservar vuelo
function reserveFlight(flightId) {
    // Mostrar el formulario de reserva
    const reservationForm = document.getElementById('reservation-form-container');
    reservationForm.style.display = 'block';

    // Guardar el ID del vuelo en un campo oculto
    document.getElementById('flight-id-input').value = flightId;

    // Actualizar el select de usuarios
    actualizarSelectUsuarios();

    // Desplazarse al formulario
    reservationForm.scrollIntoView({ behavior: 'smooth' });
}

// Función para cargar ciudades predeterminadas
function cargarCiudadesPredeterminadas() {
    console.log('Cargando ciudades predeterminadas...');
    ciudadesDisponibles = [
        { id: 1, nombreCiudad: 'Madrid', pais: 'España' },
        { id: 2, nombreCiudad: 'Barcelona', pais: 'España' },
        { id: 3, nombreCiudad: 'París', pais: 'Francia' },
        { id: 4, nombreCiudad: 'Londres', pais: 'Reino Unido' },
        { id: 5, nombreCiudad: 'Roma', pais: 'Italia' },
        { id: 6, nombreCiudad: 'Berlín', pais: 'Alemania' }
    ];
    actualizarSelectCiudades();
}

// Función para cargar usuarios predeterminados
function cargarUsuariosPredeterminados() {
    console.log('Cargando usuarios predeterminados...');
    usuariosDisponibles = [
        { id: 1, nombre: 'Ana', apellido: 'González' },
        { id: 2, nombre: 'Juan', apellido: 'Pérez' },
        { id: 3, nombre: 'María', apellido: 'López' },
        { id: 4, nombre: 'Carlos', apellido: 'Rodríguez' }
    ];
}

// Función para actualizar el select de usuarios
function actualizarSelectUsuarios() {
    const selectUsuarios = document.getElementById('user-id-input');
    const loadingIndicator = document.getElementById('user-loading');

    if (loadingIndicator) {
        loadingIndicator.style.display = 'block';
    }

    // Actualizar la opción por defecto para indicar carga
    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.textContent = 'Selecciona un usuario';

    selectUsuarios.innerHTML = '';
    selectUsuarios.appendChild(defaultOption);
    selectUsuarios.disabled = true;

    // Pequeño retraso para mostrar la animación de carga
    setTimeout(() => {
        // Agregar usuarios disponibles
        if (usuariosDisponibles && usuariosDisponibles.length > 0) {
        usuariosDisponibles.forEach(usuario => {
            if (usuario && usuario.id) {
                const option = document.createElement('option');
                option.value = usuario.id;

                // Construir el texto de la opción con manejo de valores nulos
                let displayText = '';
                if (usuario.nombre) {
                    displayText += usuario.nombre;
                }
                if (usuario.apellido) {
                    displayText += (displayText ? ' ' : '') + usuario.apellido;
                }
                displayText += (displayText ? ' ' : '') + '(ID: ' + usuario.id + ')';

                option.textContent = displayText;
                selectUsuarios.appendChild(option);
            }
        });
    } else {
        console.log('No hay usuarios disponibles para mostrar');
        // Agregar opciones de ejemplo para pruebas
        const usuarios = [
            { id: 1, nombre: 'Ana', apellido: 'González' },
            { id: 2, nombre: 'Juan', apellido: 'Pérez' },
            { id: 3, nombre: 'María', apellido: 'López' }
        ];

        usuarios.forEach(usuario => {
            const option = document.createElement('option');
            option.value = usuario.id;
            option.textContent = `${usuario.nombre} ${usuario.apellido} (ID: ${usuario.id})`;
            selectUsuarios.appendChild(option);
        });

        console.log('Se han agregado usuarios de prueba');
    }

    selectUsuarios.disabled = false;
    if (loadingIndicator) {
        loadingIndicator.style.display = 'none';
    }
}, 500);
}

// Función para enviar la reserva
function submitReservation(event) {
    event.preventDefault();

    const userId = document.getElementById('user-id-input').value;
    const flightId = document.getElementById('flight-id-input').value;

    if (!userId || !flightId) {
        alert('Por favor, complete todos los campos');
        return;
    }

    // Mostrar indicador de carga
    const submitButton = document.getElementById('submit-reservation-btn');
    const originalText = submitButton.textContent;
    submitButton.textContent = 'Procesando...';
    submitButton.disabled = true;

    // Crear objeto de datos para la solicitud
    const reservationData = {
        usuarioId: parseInt(userId),
        vueloId: parseInt(flightId)
    };

    // Enviar solicitud al servidor
    fetch('/reservas/crear', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reservationData)
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { throw new Error(text) });
        }
        return response.json();
    })
    .then(data => {
        // Ocultar el formulario
        document.getElementById('reservation-form-container').style.display = 'none';

        // Mostrar mensaje de éxito
        alert(`✈️ ¡Reserva creada exitosamente!\n\nID de Reserva: ${data.reserva.id}\nRecibirás un email de confirmación pronto.`);

        // Limpiar el formulario
        document.getElementById('reservation-form').reset();
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error al crear la reserva: ' + error.message);
    })
    .finally(() => {
        // Restaurar el botón
        submitButton.textContent = originalText;
        submitButton.disabled = false;
    });
}

// Función para cargar reservas
function fetchReservations() {
    const userId = document.getElementById('user-identifier').value;
    const container = document.getElementById('user-reservations');

    if (!userId) {
        alert('Por favor, introduce tu código de usuario');
        return;
    }

    // Mostrar indicador de carga
    container.innerHTML = '<p style="text-align: center; color: #666;">📋 Cargando reservas...</p>';

    // Obtener reservas del servidor
    fetch(`/reservas/usuario/${userId}`)
        .then(response => {
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('Usuario no encontrado');
                }
                return response.text().then(text => { throw new Error(text) });
            }
            return response.json();
        })
        .then(reservas => {
            if (reservas && reservas.length > 0) {
                let reservasHTML = '';

                reservas.forEach(reserva => {
                    const vuelo = reserva.vueloReservado;
                    const destino = vuelo && vuelo.aeropuertos && vuelo.aeropuertos.length > 0 
                        ? vuelo.aeropuertos[vuelo.aeropuertos.length - 1].ciudad.nombre 
                        : 'No disponible';
                    const aerolinea = vuelo && vuelo.aerolinea ? vuelo.aerolinea.nombre : 'No disponible';
                    const fecha = vuelo && vuelo.fecha ? vuelo.fecha.fecha : 'No disponible';

                    reservasHTML += `
                        <div style="background: white; padding: 20px; border-radius: 12px; border: 1px solid #e1e5f2; margin-top: 20px;">
                            <h4 style="color: #333; margin-bottom: 10px;">✈️ Reserva #${reserva.id}</h4>
                            <p><strong>Destino:</strong> ${destino}</p>
                            <p><strong>Fecha:</strong> ${fecha}</p>
                            <p><strong>Aerolínea:</strong> ${aerolinea}</p>
                            <p><strong>Estado:</strong> <span style="color: #28a745; font-weight: bold;">Confirmada</span></p>
                        </div>
                    `;
                });

                container.innerHTML = reservasHTML;
            } else {
                container.innerHTML = `
                    <div style="background: #f8f9fa; padding: 30px; border-radius: 12px; border: 2px dashed #dee2e6; margin-top: 20px; text-align: center;">
                        <p style="color: #6c757d; font-size: 1.1rem;">📭 No se encontraron reservas</p>
                        <p style="color: #6c757d; margin-top: 10px;">Verifica tu código de usuario o realiza tu primera reserva</p>
                    </div>
                `;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            container.innerHTML = `
                <div style="background: #f8d7da; padding: 20px; border-radius: 12px; border: 1px solid #f5c6cb; margin-top: 20px; text-align: center;">
                    <p style="color: #721c24; font-size: 1.1rem;">❌ Error al cargar las reservas</p>
                    <p style="color: #721c24; margin-top: 10px;">${error.message}</p>
                </div>
            `;
        });
}
