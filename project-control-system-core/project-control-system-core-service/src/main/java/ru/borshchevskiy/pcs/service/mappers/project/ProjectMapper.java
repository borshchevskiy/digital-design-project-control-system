package ru.borshchevskiy.pcs.service.mappers.project;

import org.springframework.stereotype.Component;
import ru.borshchevskiy.pcs.common.enums.ProjectStatus;
import ru.borshchevskiy.pcs.dto.project.ProjectDto;
import ru.borshchevskiy.pcs.entities.project.Project;

@Component
public class ProjectMapper {

    public ProjectDto mapToDto(Project project) {
        ProjectDto projectDto = new ProjectDto();

        projectDto.setId(project.getId());
        projectDto.setCode(project.getCode());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        projectDto.setStatus(project.getStatus());

        return projectDto;
    }

    public Project createProject(ProjectDto dto) {
        Project project = new Project();

        copyToProject(project, dto);

        // Новый Project должен создаваться в статусе DRAFT
        project.setStatus(ProjectStatus.DRAFT);

        return project;
    }

    public Project mergeProject(Project project, ProjectDto dto) {

        return copyToProject(project, dto);
    }

    private Project copyToProject(Project copyTo, ProjectDto copyFrom) {
        copyTo.setCode(copyFrom.getCode());
        copyTo.setName(copyFrom.getName());
        copyTo.setDescription(copyFrom.getDescription());
        copyTo.setStatus(copyFrom.getStatus());

        return copyTo;
    }
}
