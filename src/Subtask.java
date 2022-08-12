public class Subtask extends Task {

    private int idSubtaskForEpic;

    public void setIdSubtaskForEpic(int idSubtaskForEpic) {

        this.idSubtaskForEpic = idSubtaskForEpic;
    }

    public int getIdSubtaskForEpic() {

        return idSubtaskForEpic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idSubtaskForEpic=" + idSubtaskForEpic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
