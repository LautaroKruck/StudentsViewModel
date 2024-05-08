import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import java.io.File

class StudentsViewModel(
    private val fileManagement: IFiles,
    private val studentsFile: File
) {
    private var _newStudent = mutableStateOf("")
    val newStudent: State<String> get() = _newStudent

    private val _students = mutableStateListOf<String>()
    val students: List<String> get() = _students

    private var _infoMessage = mutableStateOf("")
    val infoMessage: State<String> get() = _infoMessage

    private var _showInfoMessage = mutableStateOf(false)
    val showInfoMessage: State<Boolean> get() = _showInfoMessage

    init {
        loadStudents()
    }


        private var _selectedStudentIndex = mutableStateOf(-1)
        val selectedStudentIndex: State<Int> get() = _selectedStudentIndex

        private var _editMode = mutableStateOf(false)
        val editMode: State<Boolean> get() = _editMode

        private var _editText = mutableStateOf("")
        val editText: State<String> get() = _editText

        fun selectStudent(index: Int) {
            _selectedStudentIndex.value = index
            _editText.value = students[index]
            _editMode.value = true
        }

        fun saveEditedStudent() {
            if (_selectedStudentIndex.value != -1 && _editText.value.isNotBlank()) {
                _students[_selectedStudentIndex.value] = _editText.value.trim()
                _editMode.value = false
                _selectedStudentIndex.value = -1
            }
        }

        fun cancelEdit() {
            _editMode.value = false
            _selectedStudentIndex.value = -1
        }
    fun addStudent() {
        if (_newStudent.value.isNotBlank()) {
            _students.add(_newStudent.value.trim())
            _newStudent.value = ""
        }
    }

    fun newStudentChange(name: String) {
        _newStudent.value = name
    }

    private fun loadStudents() {
        fileManagement.leer(studentsFile)?.let {
            _students.addAll(it)
        } ?: run {
            _infoMessage.value = "No se pudieron cargar los datos de los estudiantes."
            _showInfoMessage.value = true
        }
    }

    fun clearInfoMessage() {
        _infoMessage.value = ""
        _showInfoMessage.value = false
    }

    fun saveStudents() {
        val error = fileManagement.crearFic(studentsFile.absolutePath, _students.joinToString("\n"), true)
        if (error != null) {
            _infoMessage.value = "Fichero guardado correctamente"
        } else {
            _infoMessage.value = "No se pudo generar el fichero studentList.txt"
        }
        _showInfoMessage.value = true
    }
}
