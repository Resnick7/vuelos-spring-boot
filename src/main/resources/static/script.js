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
    console.log('🔄 Iniciando carga de datos de reserva...');

    fetch('/vuelos/datos-reserva')
        .then(response => {
            console.log('✅ Respuesta recibida:', response.status, response.statusText);
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('📦 Datos recibidos:', data);

            // Procesar ciudades
            if (data.ciudades && Array.isArray(data.ciudades)) {
                ciudadesDisponibles = data.ciudades;
                console.log(`🏙️ Ciudades cargadas: ${ciudadesDisponibles.length}`);
                actualizarSelectCiudades();
            } else {
                console.warn('⚠️ No se recibieron ciudades válidas');
                cargarCiudadesPredeterminadas();
            }

            // Procesar usuarios
            if (data.usuarios && Array.isArray(data.usuarios)) {
                usuariosDisponibles = data.usuarios;
                console.log(`👥 Usuarios cargados: ${usuariosDisponibles.length}`);
            } else {
                console.warn('⚠️ No se recibieron usuarios válidos');
                cargarUsuariosPredeterminados();
            }
        })
        .catch(error => {
            console.error('❌ Error al cargar datos para reserva:', error);
            cargarDatosPredeterminados();
            mostrarNotificacion('Se han cargado datos de ejemplo. Verifica la conexión con el servidor.', 'warning');
        });
}

// Función para cargar datos predeterminados en caso de error
function cargarDatosPredeterminados() {
    cargarCiudadesPredeterminadas();
    cargarUsuariosPredeterminados();
}

// Función para actualizar el select de ciudades
function actualizarSelectCiudades() {
    const selectCiudades = document.getElementById('destination-city');
    const loadingIndicator = document.getElementById('city-loading');

    if (loadingIndicator) {
        loadingIndicator.style.display = 'block';
        loadingIndicator.innerHTML = '<span>🔄 Cargando destinos...</span>';
    }

    selectCiudades.innerHTML = '<option value="">Cargando...</option>';
    selectCiudades.disabled = true;

    setTimeout(() => {
        const defaultOption = document.createElement('option');
        defaultOption.value = '';
        defaultOption.textContent = 'Selecciona tu destino';

        selectCiudades.innerHTML = '';
        selectCiudades.appendChild(defaultOption);

        if (ciudadesDisponibles && ciudadesDisponibles.length > 0) {
            // Ordenar ciudades alfabéticamente
            const ciudadesOrdenadas = [...ciudadesDisponibles].sort((a, b) =>
                a.nombreCiudad.localeCompare(b.nombreCiudad)
            );

            ciudadesOrdenadas.forEach(ciudad => {
                if (ciudad && ciudad.nombreCiudad) {
                    const option = document.createElement('option');
                    option.value = ciudad.nombreCiudad.toLowerCase();
                    option.textContent = `✈️ ${ciudad.nombreCiudad}`;
                    selectCiudades.appendChild(option);
                }
            });

            console.log(`✅ ${ciudadesDisponibles.length} ciudades agregadas al selector`);
        } else {
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
    }, 300);
}

// Función para mostrar notificaciones
function mostrarNotificacion(mensaje, tipo = 'info') {
    // Crear elemento de notificación
    const notificacion = document.createElement('div');
    notificacion.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 10000;
        padding: 15px 20px;
        border-radius: 8px;
        color: white;
        font-weight: 500;
        max-width: 400px;
        animation: slideIn 0.3s ease-out;
    `;

    // Aplicar colores según el tipo
    switch (tipo) {
        case 'success':
            notificacion.style.background = '#28a745';
            notificacion.innerHTML = `✅ ${mensaje}`;
            break;
        case 'warning':
            notificacion.style.background = '#ffc107';
            notificacion.style.color = '#212529';
            notificacion.innerHTML = `⚠️ ${mensaje}`;
            break;
        case 'error':
            notificacion.style.background = '#dc3545';
            notificacion.innerHTML = `❌ ${mensaje}`;
            break;
        default:
            notificacion.style.background = '#17a2b8';
            notificacion.innerHTML = `ℹ️ ${mensaje}`;
    }

    document.body.appendChild(notificacion);

    // Remover después de 5 segundos
    setTimeout(() => {
        notificacion.style.animation = 'slideOut 0.3s ease-in';
        setTimeout(() => {
            if (notificacion.parentNode) {
                notificacion.parentNode.removeChild(notificacion);
            }
        }, 300);
    }, 5000);
}

// Inicialización cuando se carga la página
document.addEventListener('DOMContentLoaded', function() {
    console.log('🚀 Inicializando aplicación...');

    // Configurar fecha mínima como hoy
    const fechaInput = document.getElementById('departure-date');
    if (fechaInput) {
        fechaInput.min = new Date().toISOString().split('T')[0];
    }

    // Cargar datos necesarios para reservas
    cargarDatosReserva();

    // Manejar formulario de búsqueda
    const searchForm = document.getElementById('flight-search-form');
    if (searchForm) {
        searchForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const city = document.getElementById('destination-city').value;
            const date = document.getElementById('departure-date').value;

            if (!city) {
                mostrarNotificacion('Por favor selecciona un destino', 'warning');
                return;
            }

            // La fecha ahora es opcional
            searchFlights(city, date);
        });
    }

    // Manejar formulario de perfil
    const profileForm = document.getElementById('user-profile-form');
    if (profileForm) {
        profileForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const formData = {
                firstName: document.getElementById('first-name').value,
                lastName: document.getElementById('last-name').value,
                email: document.getElementById('user-email').value,
                password: document.getElementById('user-password').value
            };

            // Simular guardado (en el futuro, enviar al servidor)
            mostrarNotificacion('Perfil actualizado correctamente', 'success');
            console.log('📝 Datos del perfil:', formData);
        });
    }

    mostrarNotificacion('¡Bienvenido a SkyBooker! 🛫', 'success');
});

// Función para buscar vuelos
function searchFlights(city, date) {
    const resultsSection = document.getElementById('flight-results');
    const container = document.getElementById('flights-container');

    // Crear mensaje de búsqueda
    let searchMessage = `Destino: ${city.toUpperCase()}`;
    if (date) {
        searchMessage += ` • Fecha: ${date}`;
    } else {
        searchMessage += ` • Todas las fechas`;
    }

    // Mostrar sección de resultados y indicador de carga
    container.innerHTML = `
        <div style="text-align: center; padding: 40px;">
            <div style="font-size: 2rem; margin-bottom: 20px;">🔍</div>
            <p style="color: #666; font-size: 1.1rem;">Buscando vuelos disponibles...</p>
            <p style="color: #999; font-size: 0.9rem;">${searchMessage}</p>
        </div>
    `;
    resultsSection.classList.add('show');

    // Realizar la búsqueda en el servidor
    const searchParams = new URLSearchParams();
    searchParams.append('ciudad', city);
    if (date) {
        searchParams.append('fecha', date);
    }

    const url = `/vuelos/buscar?${searchParams.toString()}`;
    console.log('🔍 Buscando vuelos en:', url);

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            return response.json();
        })
        .then(vuelos => {
            console.log('✈️ Vuelos encontrados:', vuelos.length);

            if (vuelos && vuelos.length > 0) {
                displayFlights(vuelos, city, date);
                mostrarNotificacion(`Se encontraron ${vuelos.length} vuelos disponibles`, 'success');
            } else {
                mostrarVuelosVacios(city, date);
                mostrarNotificacion('No se encontraron vuelos para tu búsqueda', 'warning');
            }
        })
        .catch(error => {
            console.error('❌ Error en búsqueda:', error);
            mostrarErrorBusqueda(error.message);
            mostrarNotificacion('Error al buscar vuelos. Intenta nuevamente.', 'error');
        });
}

// Función para mostrar mensaje cuando no hay vuelos
function mostrarVuelosVacios(city, date) {
    const container = document.getElementById('flights-container');
    container.innerHTML = `
        <div style="background: #f8f9fa; padding: 40px; border-radius: 15px; border: 2px dashed #dee2e6; text-align: center;">
            <div style="font-size: 3rem; margin-bottom: 20px;">🛫</div>
            <h3 style="color: #495057; margin-bottom: 15px;">No hay vuelos disponibles</h3>
            <p style="color: #6c757d; margin-bottom: 10px;">No encontramos vuelos hacia <strong>${city.toUpperCase()}</strong> para el ${date}</p>
            <p style="color: #6c757d; font-size: 0.9rem;">Intenta con otra fecha o destino</p>
            <button onclick="location.reload()" style="background: #667eea; color: white; border: none; padding: 10px 20px; border-radius: 20px; margin-top: 15px; cursor: pointer;">
                🔄 Buscar otros destinos
            </button>
        </div>
    `;
}

// Función para mostrar error en la búsqueda
function mostrarErrorBusqueda(errorMessage) {
    const container = document.getElementById('flights-container');
    container.innerHTML = `
        <div style="background: #f8d7da; padding: 30px; border-radius: 15px; border: 1px solid #f5c6cb; text-align: center;">
            <div style="font-size: 2rem; margin-bottom: 15px;">⚠️</div>
            <h3 style="color: #721c24; margin-bottom: 10px;">Error al buscar vuelos</h3>
            <p style="color: #721c24; margin-bottom: 15px;">${errorMessage}</p>
            <button onclick="location.reload()" style="background: #dc3545; color: white; border: none; padding: 10px 20px; border-radius: 20px; cursor: pointer;">
                🔄 Reintentar
            </button>
        </div>
    `;
}

// Función para mostrar vuelos
function displayFlights(vuelos, cityFilter) {
    const container = document.getElementById('flights-container');
    container.innerHTML = '';

    // Crear encabezado de resultados
    const header = document.createElement('div');
    header.style.cssText = 'margin-bottom: 20px; padding: 15px; background: #e3f2fd; border-radius: 10px; text-align: center;';
    header.innerHTML = `
        <h3 style="color: #1976d2; margin-bottom: 5px;">✈️ ${vuelos.length} vuelos encontrados</h3>
        <p style="color: #424242; font-size: 0.9rem;">Destino: ${cityFilter.toUpperCase()}</p>
    `;
    container.appendChild(header);

    vuelos.forEach((vuelo, index) => {
        const flightCard = createFlightCard(vuelo, index);
        container.appendChild(flightCard);
    });
}

// Función para crear una tarjeta de vuelo
function createFlightCard(vuelo, index, selectedDate) {
    const flightCard = document.createElement('div');
    flightCard.className = 'flight-card';

    // Obtener información del vuelo
    const aerolinea = vuelo.aerolinea ? vuelo.aerolinea.nombreAerolinea : 'Aerolínea no disponible';
    const origen = vuelo.aeropuertos && vuelo.aeropuertos.length > 0 ?
        vuelo.aeropuertos[0].ciudad?.nombreCiudad || 'Origen no disponible' : 'Origen no disponible';
    const destino = vuelo.aeropuertos && vuelo.aeropuertos.length > 1 ?
        vuelo.aeropuertos[vuelo.aeropuertos.length - 1].ciudad?.nombreCiudad || 'Destino no disponible' : 'Destino no disponible';

    // Generar horarios simulados
    const departureHour = 6 + (index * 2);
    const arrivalHour = departureHour + 2 + Math.floor(Math.random() * 3);
    const departure = `${departureHour.toString().padStart(2, '0')}:${(Math.random() * 60).toFixed(0).padStart(2, '0')}`;
    const arrival = `${arrivalHour.toString().padStart(2, '0')}:${(Math.random() * 60).toFixed(0).padStart(2, '0')}`;

    // Generar fecha si no se especificó
    let displayDate = selectedDate;
    if (!displayDate) {
        // Generar fechas futuras aleatorias
        const today = new Date();
        const futureDate = new Date(today.getTime() + (Math.random() * 30 + 1) * 24 * 60 * 60 * 1000); // 1-30 días en el futuro
        displayDate = futureDate.toISOString().split('T')[0];
    }

    // Formatear fecha para mostrar
    const formattedDate = new Date(displayDate).toLocaleDateString('es-ES', {
        weekday: 'short',
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });

    // Generar precio simulado
    const basePrice = 180 + (index * 30) + Math.floor(Math.random() * 200);
    const price = `€${basePrice}`;

    // Generar duración del vuelo
    const duration = `${2 + Math.floor(Math.random() * 4)}h ${Math.floor(Math.random() * 60)}m`;

    flightCard.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 15px;">
            <div style="flex: 1; min-width: 300px;">
                <div style="display: flex; align-items: center; margin-bottom: 10px;">
                    <span style="font-size: 1.2rem; margin-right: 8px;">✈️</span>
                    <h4 style="color: #333; margin: 0; font-size: 1.1rem;">${aerolinea}</h4>
                </div>
                <div style="color: #666; font-size: 0.9rem; line-height: 1.5;">
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 8px;">
                        <p style="margin: 0;"><strong>🛫 Origen:</strong> ${origen}</p>
                        <p style="margin: 0;"><strong>🛬 Destino:</strong> ${destino}</p>
                    </div>
                    <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px;">
                        <p style="margin: 0;"><strong>📅 Fecha:</strong> ${formattedDate}</p>
                        <p style="margin: 0;"><strong>🕐 Horario:</strong> ${departure} - ${arrival}</p>
                    </div>
                    <p style="margin: 8px 0 0 0; color: #999; font-size: 0.85rem;">
                        ⏱️ Duración: ${duration} • 🎫 Vuelo ${vuelo.id}
                    </p>
                </div>
            </div>
            <div style="text-align: right; min-width: 140px;">
                <div style="font-size: 1.8rem; font-weight: bold; color: #667eea; margin-bottom: 8px;">${price}</div>
                <div style="font-size: 0.8rem; color: #999; margin-bottom: 10px;">por persona</div>
                <button onclick="reserveFlight(${vuelo.id}, '${aerolinea}', '${origen}', '${destino}', '${price}', '${displayDate}')" 
                        style="background: linear-gradient(45deg, #28a745, #20c997); color: white; border: none; padding: 12px 24px; border-radius: 20px; cursor: pointer; font-weight: 500; transition: all 0.3s ease; font-size: 0.9rem; width: 100%;"
                        onmouseover="this.style.transform='translateY(-2px)'; this.style.boxShadow='0 8px 20px rgba(40,167,69,0.3)'"
                        onmouseout="this.style.transform='translateY(0)'; this.style.boxShadow='none'">
                    🎫 Reservar Ahora
                </button>
            </div>
        </div>
    `;

    return flightCard;
}

// Función para reservar vuelo
function reserveFlight(flightId, airline, origin, destination, price) {
    console.log('🎫 Iniciando reserva para vuelo:', flightId);

    // Mostrar el formulario de reserva
    const reservationForm = document.getElementById('reservation-form-container');
    reservationForm.style.display = 'block';

    // Actualizar información del vuelo en el formulario
    const flightInfo = document.createElement('div');
    flightInfo.style.cssText = 'background: #e8f5e8; padding: 15px; border-radius: 8px; margin-bottom: 15px; border-left: 4px solid #28a745;';
    flightInfo.innerHTML = `
        <h4 style="color: #155724; margin-bottom: 8px;">✈️ Detalles del Vuelo Seleccionado</h4>
        <p style="margin: 3px 0; color: #155724;"><strong>Aerolínea:</strong> ${airline}</p>
        <p style="margin: 3px 0; color: #155724;"><strong>Ruta:</strong> ${origin} → ${destination}</p>
        <p style="margin: 3px 0; color: #155724;"><strong>Precio:</strong> ${price}</p>
    `;

    // Insertar información del vuelo en el formulario
    const form = document.getElementById('reservation-form');
    const existingInfo = form.querySelector('.flight-info');
    if (existingInfo) {
        existingInfo.remove();
    }
    flightInfo.className = 'flight-info';
    form.insertBefore(flightInfo, form.firstChild);

    // Guardar el ID del vuelo en un campo oculto
    document.getElementById('flight-id-input').value = flightId;

    // Actualizar el select de usuarios
    actualizarSelectUsuarios();

    // Desplazarse al formulario suavemente
    reservationForm.scrollIntoView({ behavior: 'smooth', block: 'start' });

    mostrarNotificacion('Completa tus datos para confirmar la reserva', 'info');
}

// Función para cargar ciudades predeterminadas
function cargarCiudadesPredeterminadas() {
    console.log('🏙️ Cargando ciudades predeterminadas...');
    ciudadesDisponibles = [
        { id: 1, nombreCiudad: 'Madrid' },
        { id: 2, nombreCiudad: 'Barcelona' },
        { id: 3, nombreCiudad: 'París' },
        { id: 4, nombreCiudad: 'Londres' },
        { id: 5, nombreCiudad: 'Roma' },
        { id: 6, nombreCiudad: 'Berlín' },
        { id: 7, nombreCiudad: 'Ámsterdam' },
        { id: 8, nombreCiudad: 'Lisboa' },
        { id: 9, nombreCiudad: 'Viena' },
        { id: 10, nombreCiudad: 'Nueva York' }
    ];
    actualizarSelectCiudades();
}

// Función para cargar usuarios predeterminados
function cargarUsuariosPredeterminados() {
    console.log('👥 Cargando usuarios predeterminados...');
    usuariosDisponibles = [
        { id: 1, nombrePersona: 'Ana', apellidoPersona: 'González' },
        { id: 2, nombrePersona: 'Juan', apellidoPersona: 'Pérez' },
        { id: 3, nombrePersona: 'María', apellidoPersona: 'López' },
        { id: 4, nombrePersona: 'Carlos', apellidoPersona: 'Rodríguez' },
        { id: 5, nombrePersona: 'Laura', apellidoPersona: 'Martínez' }
    ];
}

// Función para actualizar el select de usuarios
function actualizarSelectUsuarios() {
    const selectUsuarios = document.getElementById('user-id-input');
    const loadingIndicator = document.getElementById('user-loading');

    if (loadingIndicator) {
        loadingIndicator.style.display = 'block';
        loadingIndicator.innerHTML = '<span>🔄 Cargando usuarios...</span>';
    }

    selectUsuarios.innerHTML = '<option value="">Cargando...</option>';
    selectUsuarios.disabled = true;

    setTimeout(() => {
        const defaultOption = document.createElement('option');
        defaultOption.value = '';
        defaultOption.textContent = 'Selecciona tu perfil';

        selectUsuarios.innerHTML = '';
        selectUsuarios.appendChild(defaultOption);

        if (usuariosDisponibles && usuariosDisponibles.length > 0) {
            usuariosDisponibles.forEach(usuario => {
                if (usuario && usuario.id) {
                    const option = document.createElement('option');
                    option.value = usuario.id;

                    // Construir el texto de la opción
                    let displayText = '';
                    if (usuario.nombrePersona) {
                        displayText += usuario.nombrePersona;
                    }
                    if (usuario.apellidoPersona) {
                        displayText += (displayText ? ' ' : '') + usuario.apellidoPersona;
                    }
                    displayText += ` (ID: ${usuario.id})`;

                    option.textContent = displayText;
                    selectUsuarios.appendChild(option);
                }
            });
            console.log(`✅ ${usuariosDisponibles.length} usuarios agregados al selector`);
        } else {
            const noDataOption = document.createElement('option');
            noDataOption.value = '';
            noDataOption.textContent = 'No hay usuarios disponibles';
            noDataOption.disabled = true;
            selectUsuarios.appendChild(noDataOption);
        }

        selectUsuarios.disabled = false;
        if (loadingIndicator) {
            loadingIndicator.style.display = 'none';
        }
    }, 300);
}

// Función para enviar la reserva
function submitReservation(event) {
    event.preventDefault();

    const userId = document.getElementById('user-id-input').value;
    const flightId = document.getElementById('flight-id-input').value;

    if (!userId || !flightId) {
        mostrarNotificacion('Por favor, complete todos los campos requeridos', 'warning');
        return;
    }

    // Mostrar indicador de carga
    const submitButton = document.getElementById('submit-reservation-btn');
    const originalText = submitButton.textContent;
    submitButton.textContent = '⏳ Procesando reserva...';
    submitButton.disabled = true;

    // Crear objeto de datos para la solicitud
    const reservationData = {
        usuarioId: parseInt(userId),
        vueloId: parseInt(flightId)
    };

    console.log('📤 Enviando reserva:', reservationData);

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
                return response.text().then(text => {
                    throw new Error(`HTTP ${response.status}: ${text}`)
                });
            }
            return response.json();
        })
        .then(data => {
            console.log('✅ Reserva creada exitosamente:', data);

            // Ocultar el formulario
            document.getElementById('reservation-form-container').style.display = 'none';

            // Mostrar mensaje de éxito detallado
            mostrarModalExito(data);

            // Limpiar el formulario
            document.getElementById('reservation-form').reset();

            // Remover información del vuelo del formulario
            const flightInfo = document.querySelector('.flight-info');
            if (flightInfo) {
                flightInfo.remove();
            }
        })
        .catch(error => {
            console.error('❌ Error al crear reserva:', error);
            mostrarNotificacion(`Error al crear la reserva: ${error.message}`, 'error');
        })
        .finally(() => {
            // Restaurar el botón
            submitButton.textContent = originalText;
            submitButton.disabled = false;
        });
}

// Función para mostrar modal de éxito
function mostrarModalExito(data) {
    const modal = document.createElement('div');
    modal.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0,0,0,0.5);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 10000;
    `;

    const modalContent = document.createElement('div');
    modalContent.style.cssText = `
        background: white;
        padding: 30px;
        border-radius: 15px;
        max-width: 500px;
        width: 90%;
        text-align: center;
        box-shadow: 0 20px 40px rgba(0,0,0,0.1);
    `;

    modalContent.innerHTML = `
        <div style="font-size: 4rem; margin-bottom: 20px;">🎉</div>
        <h2 style="color: #28a745; margin-bottom: 15px;">¡Reserva Confirmada!</h2>
        <p style="color: #666; margin-bottom: 20px;">Tu vuelo ha sido reservado exitosamente</p>
        <div style="background: #f8f9fa; padding: 15px; border-radius: 8px; margin-bottom: 20px; text-align: left;">
            <p style="margin: 5px 0;"><strong>ID de Reserva:</strong> #${data.reserva?.id || 'N/A'}</p>
            <p style="margin: 5px 0;"><strong>Estado:</strong> <span style="color: #28a745;">Confirmada</span></p>
        </div>
        <p style="color: #666; font-size: 0.9rem; margin-bottom: 20px;">
            Recibirás un email de confirmación con todos los detalles de tu vuelo.
        </p>
        <button onclick="this.parentElement.parentElement.remove()" 
                style="background: #28a745; color: white; border: none; padding: 12px 30px; border-radius: 25px; cursor: pointer; font-weight: 500;">
            ✅ Entendido
        </button>
    `;

    modal.appendChild(modalContent);
    document.body.appendChild(modal);

    // Remover modal al hacer clic fuera
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            modal.remove();
        }
    });
}

// Función para cargar reservas
function fetchReservations() {
    const userId = document.getElementById('user-identifier').value;
    const container = document.getElementById('user-reservations');

    if (!userId) {
        mostrarNotificacion('Por favor, introduce tu código de usuario', 'warning');
        return;
    }

    // Mostrar indicador de carga
    container.innerHTML = `
        <div style="text-align: center; padding: 40px;">
            <div style="font-size: 2rem; margin-bottom: 15px;">📋</div>
            <p style="color: #666; font-size: 1.1rem;">Cargando tus reservas...</p>
        </div>
    `;

    console.log('🔍 Buscando reservas para usuario:', userId);

    // Obtener reservas del servidor
    fetch(`/reservas/usuario/${userId}`)
        .then(response => {
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('Usuario no encontrado');
                }
                return response.text().then(text => {
                    throw new Error(`HTTP ${response.status}: ${text}`)
                });
            }
            return response.json();
        })
        .then(reservas => {
            console.log('📋 Reservas encontradas:', reservas.length);

            if (reservas && reservas.length > 0) {
                mostrarReservas(reservas);
                mostrarNotificacion(`Se encontraron ${reservas.length} reservas`, 'success');
            } else {
                mostrarReservasVacias();
                mostrarNotificacion('No se encontraron reservas para este usuario', 'info');
            }
        })
        .catch(error => {
            console.error('❌ Error al cargar reservas:', error);
            mostrarErrorReservas(error.message);
            mostrarNotificacion('Error al cargar las reservas', 'error');
        });
}

// Función para mostrar reservas
function mostrarReservas(reservas) {
    const container = document.getElementById('user-reservations');

    let reservasHTML = `
        <div style="background: #e8f5e8; padding: 15px; border-radius: 10px; margin: 20px 0; text-align: center;">
            <h3 style="color: #155724; margin-bottom: 5px;">✅ Reservas Encontradas</h3>
            <p style="color: #155724; font-size: 0.9rem;">Total: ${reservas.length} reserva(s)</p>
        </div>
    `;

    reservas.forEach((reserva, index) => {
        const vuelo = reserva.vueloReservado;
        const destino = vuelo && vuelo.aeropuertos && vuelo.aeropuertos.length > 0
            ? vuelo.aeropuertos[vuelo.aeropuertos.length - 1].ciudad?.nombreCiudad || 'No disponible'
            : 'No disponible';
        const origen = vuelo && vuelo.aeropuertos && vuelo.aeropuertos.length > 1
            ? vuelo.aeropuertos[0].ciudad?.nombreCiudad || 'No disponible'
            : 'No disponible';
        const aerolinea = vuelo && vuelo.aerolinea ? vuelo.aerolinea.nombreAerolinea : 'No disponible';

        reservasHTML += `
            <div style="background: white; padding: 25px; border-radius: 15px; border: 1px solid #e1e5f2; margin-top: 15px; box-shadow: 0 2px 10px rgba(0,0,0,0.05);">
                <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 15px;">
                    <h4 style="color: #333; margin: 0; font-size: 1.2rem;">✈️ Reserva #${reserva.id}</h4>
                    <span style="background: #28a745; color: white; padding: 4px 12px; border-radius: 15px; font-size: 0.8rem; font-weight: 500;">
                        ✅ Confirmada
                    </span>
                </div>
                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 15px; color: #666;">
                    <div>
                        <p style="margin: 8px 0;"><strong style="color: #333;">🛫 Origen:</strong> ${origen}</p>
                        <p style="margin: 8px 0;"><strong style="color: #333;">🛬 Destino:</strong> ${destino}</p>
                    </div>
                    <div>
                        <p style="margin: 8px 0;"><strong style="color: #333;">🏢 Aerolínea:</strong> ${aerolinea}</p>
                        <p style="margin: 8px 0;"><strong style="color: #333;">🎫 ID Vuelo:</strong> ${vuelo?.id || 'N/A'}</p>
                    </div>
                </div>
                <div style="margin-top: 15px; padding-top: 15px; border-top: 1px solid #e9ecef;">
                    <button onclick="mostrarDetallesReserva(${reserva.id})" 
                            style="background: #667eea; color: white; border: none; padding: 8px 16px; border-radius: 15px; cursor: pointer; font-size: 0.9rem; margin-right: 10px;">
                        📋 Ver Detalles
                    </button>
                    <button onclick="confirmarCancelacion(${reserva.id})" 
                            style="background: #dc3545; color: white; border: none; padding: 8px 16px; border-radius: 15px; cursor: pointer; font-size: 0.9rem;">
                        ❌ Cancelar
                    </button>
                </div>
            </div>
        `;
    });

    container.innerHTML = reservasHTML;
}

// Función para mostrar mensaje cuando no hay reservas
function mostrarReservasVacias() {
    const container = document.getElementById('user-reservations');
    container.innerHTML = `
        <div style="background: #f8f9fa; padding: 40px; border-radius: 15px; border: 2px dashed #dee2e6; margin-top: 20px; text-align: center;">
            <div style="font-size: 3rem; margin-bottom: 20px;">📭</div>
            <h3 style="color: #495057; margin-bottom: 15px;">No tienes reservas aún</h3>
            <p style="color: #6c757d; margin-bottom: 20px;">¡Es el momento perfecto para planear tu próximo viaje!</p>
            <button onclick="switchSection('search')" 
                    style="background: #667eea; color: white; border: none; padding: 12px 25px; border-radius: 25px; cursor: pointer; font-weight: 500;">
                🔍 Buscar Vuelos
            </button>
        </div>
    `;
}

// Función para mostrar error al cargar reservas
function mostrarErrorReservas(errorMessage) {
    const container = document.getElementById('user-reservations');
    container.innerHTML = `
        <div style="background: #f8d7da; padding: 30px; border-radius: 15px; border: 1px solid #f5c6cb; margin-top: 20px; text-align: center;">
            <div style="font-size: 2rem; margin-bottom: 15px;">⚠️</div>
            <h3 style="color: #721c24; margin-bottom: 10px;">Error al cargar reservas</h3>
            <p style="color: #721c24; margin-bottom: 15px;">${errorMessage}</p>
            <p style="color: #721c24; font-size: 0.9rem; margin-bottom: 20px;">
                Verifica tu código de usuario o intenta nuevamente
            </p>
            <button onclick="fetchReservations()" 
                    style="background: #dc3545; color: white; border: none; padding: 10px 20px; border-radius: 20px; cursor: pointer;">
                🔄 Reintentar
            </button>
        </div>
    `;
}

// Función para debugear datos
function debugearDatos() {
    console.log('🔍 Iniciando debug de datos...');

    fetch('/admin/debug-datos')
        .then(response => {
            console.log('📊 Respuesta de debug recibida:', response.status);
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('📊 Datos de debug:', data);

            let mensaje = '📊 ESTADO DE LA BASE DE DATOS\n\n';
            mensaje += `🏙️ Ciudades: ${data.conteos.ciudades}\n`;
            mensaje += `👥 Usuarios: ${data.conteos.usuarios}\n`;
            mensaje += `🏢 Aerolíneas: ${data.conteos.aerolineas}\n`;
            mensaje += `🛬 Aeropuertos: ${data.conteos.aeropuertos}\n`;
            mensaje += `✈️ Vuelos: ${data.conteos.vuelos}\n\n`;

            if (data.conteos.vuelos === 0) {
                mensaje += '❌ PROBLEMA: No hay vuelos en la base de datos\n';
                mensaje += 'Esto explica por qué no encuentras vuelos al buscar.\n\n';
                mensaje += '🔧 SOLUCIONES:\n';
                mensaje += '1. Reinicia la aplicación\n';
                mensaje += '2. Verifica los logs del servidor\n';
                mensaje += '3. Usa /h2-console para verificar las tablas';
            } else {
                mensaje += '✅ Estado: Datos disponibles\n';
                mensaje += `Ejemplo de vuelos:\n`;
                data.ejemploVuelos.forEach((vuelo, i) => {
                    mensaje += `  ${i+1}. ${vuelo.aerolinea} - ${vuelo.origen || 'N/A'} → ${vuelo.destino || 'N/A'}\n`;
                });
            }

            alert(mensaje);

            // También mostrar en consola para más detalles
            console.table(data.conteos);
            console.log('Ejemplos de vuelos:', data.ejemploVuelos);

        })
        .catch(error => {
            console.error('❌ Error en debug de datos:', error);
            alert('Error al obtener información de debug: ' + error.message);
        });
}

// Función para confirmar cancelación (placeholder)
function confirmarCancelacion(reservaId) {
    if (confirm('¿Estás seguro de que quieres cancelar esta reserva?')) {
        mostrarNotificacion(`Reserva #${reservaId} cancelada (funcionalidad en desarrollo)`, 'warning');
        console.log('❌ Cancelando reserva:', reservaId);
    }
}

// Agregar estilos CSS para las animaciones
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            opacity: 0;
            transform: translateX(100%);
        }
        to {
            opacity: 1;
            transform: translateX(0);
        }
    }
    
    @keyframes slideOut {
        from {
            opacity: 1;
            transform: translateX(0);
        }
        to {
            opacity: 0;
            transform: translateX(100%);
        }
    }
    
    .flight-card {
        transition: all 0.3s ease;
    }
    
    .flight-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 10px 30px rgba(0,0,0,0.15) !important;
    }
`;
document.head.appendChild(style);