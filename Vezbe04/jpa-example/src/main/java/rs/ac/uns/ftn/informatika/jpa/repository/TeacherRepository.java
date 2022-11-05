package rs.ac.uns.ftn.informatika.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rs.ac.uns.ftn.informatika.jpa.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
	
	@Query("select t from Teacher t join fetch t.courses e where t.id =?1")
	public Teacher findOneWithCourses(Integer teacherId);

}
