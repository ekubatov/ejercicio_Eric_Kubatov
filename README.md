# ejercicio_Eric_Kubatov

# Consigna

Existe un call center donde hay 3 tipos de empleados: operador, supervisor
y director. El proceso de la atención de una llamada telefónica en primera
instancia debe ser atendida por un operador, si no hay ninguno libre debe
ser atendida por un supervisor, y de no haber tampoco supervisores libres
debe ser atendida por un director.

# Requerimiento

- Debe existir una clase Dispatcher encargada de manejar las
llamadas, y debe contener el método dispatchCall para que las
asigne a los empleados disponibles.

- El método dispatchCall puede invocarse por varios hilos al mismo
tiempo.

- La clase Dispatcher debe tener la capacidad de poder procesar 10
llamadas al mismo tiempo (de modo concurrente).

- Cada llamada puede durar un tiempo aleatorio entre 5 y 10
segundos.

- Debe tener un test unitario donde lleguen 10 llamadas.

# Extras/Plus

- Dar alguna solución sobre qué pasa con una llamada cuando no hay
ningún empleado libre.

- Dar alguna solución sobre qué pasa con una llamada cuando entran
más de 10 llamadas concurrentes.

- Agregar los tests unitarios que se crean convenientes.

- Agregar documentación de código

# RESOLUCIÓN PRINCIPAL

En este ejercicio al haber tareas que pueden ejecutarse de manera simultánea, la solución fue utilizar Threads. Tanto en la clase empleado (Employee), como en el despachador (Dispatcher), que es el encargado de asignar las llamadas a medida que estan disponibles los empleados, y en la clase productora de llamadas, se implementó la interface Runnable y se sobre-escribe  el método  "run()". Este método codifica la funcionalidad que se ejecuta en un hilo, es decir lo que se va a ejecutar de forma secuencial en un hilo.

Para manejar las llamadas, se colocan en una cola concurrente y esperan hasta que algún empleado esté disponible.

Con la clase Executors, con en el que en el método "newFixedThreadPool()" le estamos diciendo que fije como número de threads a ejecutar (empleados); es decir, que nos llevará la gestión de los procesos a ejecutar en los threads del programa. Lo de "ThreadPool" es porque este Framework Executors parte del concepto de que gestiona una piscina (pool) de threads.

Una vez declarado el "executor",  lanzamos los procesos (es decir las llamadas se ponen en cola) para procesarlos. 

# RESOLUCIÓN EXTRAS/PLUS

CASO NO HAY NINGUN EMPLEADO LIBRE: Se utiliza una lista llamadas entrantes de tipo ConcurrentLinkedDeque, lo cual permitirá realizar operaciones concurrentes . Esta lista permite que las llamadas vayan encolando. Cuando no hay empleados libres, la llamada entrante se mantiene en la lista y será atendida cuando un empleado este disponible.

CASO ENTRAN MAS DE 10 LLAMADAS AL MISMO TIEMPO: Para este caso se utiliza dos estados (AVAILABLE/BUSY) indicando si un empleado está o no disponible para atender la llamada. Cuando hay una llamada entrante y están todos los empleados ocupados, el proceso se queda esperando hasta que uno sea liberado 



