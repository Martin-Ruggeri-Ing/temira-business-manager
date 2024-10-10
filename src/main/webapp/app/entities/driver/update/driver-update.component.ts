import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IDriver } from '../driver.model';
import { DriverService } from '../service/driver.service';
import { DriverFormGroup, DriverFormService } from './driver-form.service';

@Component({
  standalone: true,
  selector: 'jhi-driver-update',
  templateUrl: './driver-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DriverUpdateComponent implements OnInit {
  isSaving = false;
  driver: IDriver | null = null;

  usersSharedCollection: IUser[] = [];

  protected driverService = inject(DriverService);
  protected driverFormService = inject(DriverFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DriverFormGroup = this.driverFormService.createDriverFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ driver }) => {
      this.driver = driver;
      if (driver) {
        this.updateForm(driver);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const driver = this.driverFormService.getDriver(this.editForm);
    if (driver.id !== null) {
      this.subscribeToSaveResponse(this.driverService.update(driver));
    } else {
      this.subscribeToSaveResponse(this.driverService.create(driver));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDriver>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(driver: IDriver): void {
    this.driver = driver;
    this.driverFormService.resetForm(this.editForm, driver);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, driver.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.driver?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
