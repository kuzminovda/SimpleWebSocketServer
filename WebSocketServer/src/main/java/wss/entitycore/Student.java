package wss.entitycore;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import wss.annotations.ExcelField;



@Entity
public class Student
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelField (name= "Номер в БД", index = 1)
    private Long id;  // Идентификатор записи студента

    @Basic
    @ExcelField (name= "Фамилия", index = 2)
    private String lastName; 
    
    @Basic
    @ExcelField (name= "Имя", index = 3)
    private String firstName;
    
    @Basic
    @ExcelField (name= "Отчество", index = 4)
    private String middleName;

    @Basic
    private int yearOfstudy;

    @Basic
    private String studentGroup;

    @Lob
    @Basic
    private byte[] photo;

    /**
     * Метод получения идентификатора студента
     *
     * @return идентификатор студента
     */
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getMiddleName()
    {
        return middleName;
    }

    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    public int getYearOfstudy()
    {
        return yearOfstudy;
    }

    public void setYearOfstudy(int yearOfstudy)
    {
        this.yearOfstudy = yearOfstudy;
    }

    public String getStudentGroup()
    {
        return studentGroup;
    }

    public void setStudentGroup(String studentGroup)
    {
        this.studentGroup = studentGroup;
    }

    public byte[] getPhoto()
    {
        return photo;
    }

    public void setPhoto(byte[] photo)
    {
        this.photo = photo;
    }

    @Override
    public String toString()
    {
        return ("Студент id = {" + id + "} ==>" + lastName + " " + firstName + " " + middleName + " курс {" + yearOfstudy + "}  группа: {" + studentGroup + "}");
    }

}
