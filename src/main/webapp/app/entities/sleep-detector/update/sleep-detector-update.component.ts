import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ISleepDetector } from '../sleep-detector.model';
import { SleepDetectorService } from '../service/sleep-detector.service';
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

  usersSharedCollection: IUser[] = [];

  protected sleepDetectorService = inject(SleepDetectorService);
  protected sleepDetectorFormService = inject(SleepDetectorFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SleepDetectorFormGroup = this.sleepDetectorFormService.createSleepDetectorFormGroup();

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

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, sleepDetector.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.sleepDetector?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
