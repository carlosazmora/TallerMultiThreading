import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CustomThreadExample {
    public static void main(String[] args) {

        System.out.println("Simulando el control de una máquina dispensadora utilizando multi-threading...");

        // Componentes de la máquina simulados
        Callable<String> dispensarProducto = () -> {
            System.out.println("Dispensando producto...");
            Thread.sleep(1000); // Tiempo de dispensado
            return "Producto dispensado correctamente.";
        };

        Callable<String> verificarPeso = () -> {
            System.out.println("Verificando peso...");
            Thread.sleep(500); // Simulación del tiempo de verificación
            return Math.random() > 0.5 ? "Peso correcto." : "Error: peso incorrecto.";
        };

        // Lista de tareas para ejecutar en paralelo
        List<Callable<String>> tasks = Arrays.asList(verificarPeso);
        if(verificarPeso.equals("Peso correcto.")){
            tasks = Arrays.asList(dispensarProducto);
        }

        // Crear un grupo de hilos
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            //Envía tareas al Executor. Se ejecutan simultáneamente
            Future<String> resultadoDispensado = executor.submit(dispensarProducto);
            Future<String> resultadoPeso = executor.submit(verificarPeso);

            // Verifica el peso del producto
            String pesoResultado = resultadoPeso.get(); // Reporta el resultado de verificarPeso
            System.out.println(pesoResultado);

            // Revisa si el peso es correcto y dispensa el producto en ese caso
            if ("Peso correcto.".equals(pesoResultado)) {
                System.out.println(resultadoDispensado.get()); // Imprime el resultado obtenido de dispensarProducto
            } else {
                System.out.println("No se dispensó el producto debido a peso incorrecto.");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}