import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/vehicle/service/vehicle.service';
import { IDriver } from 'app/entities/driver/driver.model';
import { DriverService } from 'app/entities/driver/service/driver.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { SleepDetectorService } from '../service/sleep-detector.service';
import { ISleepDetector } from '../sleep-detector.model';
import { SleepDetectorFormGroup, SleepDetectorFormService } from './sleep-detector-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sleep-detector-update',
  templateUrl: './sleep-detector-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SleepDetectorUpdateComponent implements OnInit {
  isSaving = false;
  sleepDetector: ISleepDetector | null = null;

  vehiclesCollection: IVehicle[] = [];
  driversCollection: IDriver[] = [];
  usersSharedCollection: IUser[] = [];

  protected sleepDetectorService = inject(SleepDetectorService);
  protected sleepDetectorFormService = inject(SleepDetectorFormService);
  protected vehicleService = inject(VehicleService);
  protected driverService = inject(DriverService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SleepDetectorFormGroup = this.sleepDetectorFormService.createSleepDetectorFormGroup();

  compareVehicle = (o1: IVehicle | null, o2: IVehicle | null): boolean => this.vehicleService.compareVehicle(o1, o2);

  compareDriver = (o1: IDriver | null, o2: IDriver | null): boolean => this.driverService.compareDriver(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sleepDetector }) => {
      this.sleepDetector = sleepDetector;
      if (sleepDetector) {
        this.updateForm(sleepDetector);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sleepDetector = this.sleepDetectorFormService.getSleepDetector(this.editForm);
    if (sleepDetector.id !== null) {
      this.subscribeToSaveResponse(this.sleepDetectorService.update(sleepDetector));
    } else {
      this.subscribeToSaveResponse(this.sleepDetectorService.create(sleepDetector));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISleepDetector>>): void {
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

  protected updateForm(sleepDetector: ISleepDetector): void {
    this.sleepDetector = sleepDetector;
    this.sleepDetectorFormService.resetForm(this.editForm, sleepDetector);

    this.vehiclesCollection = this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(this.vehiclesCollection, sleepDetector.vehicle);
    this.driversCollection = this.driverService.addDriverToCollectionIfMissing<IDriver>(this.driversCollection, sleepDetector.driver);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, sleepDetector.user);
  }

  protected loadRelationshipsOptions(): void {
    this.vehicleService
      .query({ filter: 'sleepdetector-is-null' })
      .pipe(map((res: HttpResponse<IVehicle[]>) => res.body ?? []))
      .pipe(
        map((vehicles: IVehicle[]) => this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(vehicles, this.sleepDetector?.vehicle)),
      )
      .subscribe((vehicles: IVehicle[]) => (this.vehiclesCollection = vehicles));

    this.driverService
      .query({ filter: 'sleepdetector-is-null' })
      .pipe(map((res: HttpResponse<IDriver[]>) => res.body ?? []))
      .pipe(map((drivers: IDriver[]) => this.driverService.addDriverToCollectionIfMissing<IDriver>(drivers, this.sleepDetector?.driver)))
      .subscribe((drivers: IDriver[]) => (this.driversCollection = drivers));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.sleepDetector?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
