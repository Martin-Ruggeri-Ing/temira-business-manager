import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISleepDetector } from '../sleep-detector.model';

@Component({
  standalone: true,
  selector: 'jhi-sleep-detector-detail',
  templateUrl: './sleep-detector-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SleepDetectorDetailComponent {
  sleepDetector = input<ISleepDetector | null>(null);

  previousState(): void {
    window.history.back();
  }
}
