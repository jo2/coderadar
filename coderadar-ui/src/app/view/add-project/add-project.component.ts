import {Component} from '@angular/core';
import {Project} from '../../model/project';
import {ProjectService} from '../../service/project.service';
import {Router} from '@angular/router';
import {UserService} from '../../service/user.service';
import {BAD_REQUEST, CONFLICT, FORBIDDEN, INTERNAL_SERVER_ERROR} from 'http-status-codes';
import {Title} from '@angular/platform-browser';

@Component({
  selector: 'app-add-project',
  templateUrl: './add-project.component.html',
  styleUrls: ['./add-project.component.scss']
})
export class AddProjectComponent {

  project: Project;

  incorrectURL = false;
  projectExists = false;
  nameEmpty = false;
  defaultBranchEmpty = false;

  constructor(private router: Router, private userService: UserService, private projectService: ProjectService,
              private titleService: Title) {
    this.project = new Project();
    this.project.name = '';
    this.project.vcsUrl = '';
    this.project.defaultBranch = 'master';
    titleService.setTitle('Coderadar - Add project');
  }

  /**
   * Validates user input and calls ProjectService.addProject().
   * Handles server errors.
   * If access is denied (403) sends the refresh token and tries to submit again.
   */
  submitForm() {
    if (this.validateInput()) {
      return;
    }

    console.log(this.project.buildCommand);
    // @ts-ignore
    this.project.buildCommand = this.project.buildCommand.replaceAll('\&', '%26');
    console.log(this.project.buildCommand);

    this.projectService.addProject(this.project)
      .then(response => {
        this.project.id = response.body.id;
        this.router.navigate(['/project-configure', this.project.id]);
      })
      .catch(error => {
        if (error.status && error.status === FORBIDDEN) { // If access is denied
          this.userService.refresh(() => this.submitForm());
        } else if (error.status && error.status === BAD_REQUEST) {   // If there is a field error
          if (error.error && error.error.errorMessage === 'Validation Error') {
            error.error.fieldErrors.forEach(field => {  // Check which field
              if (field.field === 'vcsUrl') {
                this.incorrectURL = true;
              }
            });
          }
        } else if (error.status === CONFLICT &&
          error.error.errorMessage === 'The project ' + this.project.name + ' already exists.') {
          this.projectExists = true;
        }
      });
  }

  /**
   * Checks for empty form fields.
   */
  private validateInput(): boolean {
    this.projectExists = false;
    this.defaultBranchEmpty = false;

    this.incorrectURL = this.project.vcsUrl.trim().length === 0;
    this.nameEmpty = this.project.name.trim().length === 0;
    this.defaultBranchEmpty = this.project.defaultBranch.trim().length === 0;

    return this.nameEmpty || this.incorrectURL || this.defaultBranchEmpty;
  }
}
