package OptimAlgorithms;

public class Cargoltem {
    public String id;
    public String type;
    public double weight;
    public double volume;
    public double value;
    public String priority;
    
    public Cargoltem(String id, String type, double weight, double volume, double value, String priority) {
        this.id = id;
        this.type = type;
        this.weight = weight;
        this.volume = volume;
        this.value = value;
        this.priority = priority;
    }
    
    @Override
    public String toString() {
        return String.format("Cargoltem{id='%s', type='%s', weight=%.1fkg, value=$%.2f, priority='%s'}", 
                           id, type, weight, value, priority);
    }
}