package org.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        var controller = new SimulationController();
        var orders = SimulationController.sampleOrders();

        controller.runNoTrace(orders);   // 1) tanpa trace id
        controller.runWithTrace(orders); // 2) dengan trace id
    }
}
