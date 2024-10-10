import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISleepDetector } from 'app/entities/sleep-detector/sleep-detector.model';
import { SleepDetectorService } from 'app/entities/sleep-detector/service/sleep-detector.service';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/vehicle/service/vehicle.service';
import { IDriver } from 'app/entities/driver/driver.model';
import { DriverService } from 'app/entities/driver/service/driver.service';
import { StatementService } from '../service/statement.service';
import { IStatement } from '../statement.model';
import { StatementFormGroup, StatementFormService } from './statement-form.service';

@Component({
  standalone: true,
  selector: 'jhi-statement-update',
  templateUrl: './statement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StatementUpdateComponent implements OnInit {
  isSaving = false;
  statement: IStatement | null = null;

  sleepDetectorsSharedCollection: ISleepDetector[] = [];
  vehiclesSharedCollection: IVehicle[] = [];
  driversSharedCollection: IDriver[] = [];

  protected statementService = inject(StatementService);
  protected statementFormService = inject(StatementFormService);
  protected sleepDetectorService = inject(SleepDetectorService);
  protected vehicleService = inject(VehicleService);
  protected driverService = inject(DriverService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: StatementFormGroup = this.statementFormService.createStatementFormGroup();

  compareSleepDetector = (o1: ISleepDetector | null, o2: ISleepDetector | null): boolean =>
    this.sleepDetectorService.compareSleepDetector(o1, o2);

  compareVehicle = (o1: IVehicle | null, o2: IVehicle | null): boolean => this.vehicleService.compareVehicle(o1, o2);

  compareDriver = (o1: IDriver | null, o2: IDriver | null): boolean => this.driverService.compareDriver(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ statement }) => {
      this.statement = statement;
      if (statement) {
        this.updateForm(statement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const statement = this.statementFormService.getStatement(this.editForm);
    if (statement.id !== null) {
      this.subscribeToSaveResponse(this.statementService.update(statement));
    } else {
      this.subscribeToSaveResponse(this.statementService.create(statement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStatement>>): void {
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

  protected updateForm(statement: IStatement): void {
    this.statement = statement;
    this.statementFormService.resetForm(this.editForm, statement);

    this.sleepDetectorsSharedCollection = this.sleepDetectorService.addSleepDetectorToCollectionIfMissing<ISleepDetector>(
      this.sleepDetectorsSharedCollection,
      statement.sleepDetector,
    );
    this.vehiclesSharedCollection = this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(
      this.vehiclesSharedCollection,
      statement.vehicle,
    );
    this.driversSharedCollection = this.driverService.addDriverToCollectionIfMissing<IDriver>(
      this.driversSharedCollection,
      statement.driver,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sleepDetectorService
      .query()
      .pipe(map((res: HttpResponse<ISleepDetector[]>) => res.body ?? []))
      .pipe(
        map((sleepDetectors: ISleepDetector[]) =>
          this.sleepDetectorService.addSleepDetectorToCollectionIfMissing<ISleepDetector>(sleepDetectors, this.statement?.sleepDetector),
        ),
      )
      .subscribe((sleepDetectors: ISleepDetector[]) => (this.sleepDetectorsSharedCollection = sleepDetectors));

    this.vehicleService
      .query()
      .pipe(map((res: HttpResponse<IVehicle[]>) => res.body ?? []))
      .pipe(map((vehicles: IVehicle[]) => this.vehicleService.addVehicleToCollectionIfMissing<IVehicle>(vehicles, this.statement?.vehicle)))
      .subscribe((vehicles: IVehicle[]) => (this.vehiclesSharedCollection = vehicles));

    this.driverService
      .query()
      .pipe(map((res: HttpResponse<IDriver[]>) => res.body ?? []))
      .pipe(map((drivers: IDriver[]) => this.driverService.addDriverToCollectionIfMissing<IDriver>(drivers, this.statement?.driver)))
      .subscribe((drivers: IDriver[]) => (this.driversSharedCollection = drivers));
  }
}
