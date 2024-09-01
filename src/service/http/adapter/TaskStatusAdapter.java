package service.http.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.TaskStatus;

import java.io.IOException;

public class TaskStatusAdapter extends TypeAdapter<TaskStatus> {

    @Override
    public void write(JsonWriter jsonWriter, TaskStatus status) throws IOException {
        if (status == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(status.toString());
        }
    }

    @Override
    public TaskStatus read(JsonReader jsonReader) throws IOException {
        String str = jsonReader.nextString();
        if (str.equals("null")) {
            return null;
        }
        return TaskStatus.valueOf(str);
    }
}
