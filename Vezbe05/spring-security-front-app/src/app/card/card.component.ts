import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {

  expand = false;

  @Input() title!: string;
  @Input() subTitle!: string;
  @Input() imgUrl!: string;
  @Input() content!: string;
  @Input() apiText!: string;
  @Input() responseObj: any;

  @Output() apiClick: EventEmitter<any> = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  onButtonClick() {
    this.expand = true;
    this.apiClick.next(this.apiText);
  }

  toggleExpand() {
    this.expand = !this.expand;
  }

  responsePanelClass() {
    let classes = ['response'];

    if (this.expand) {
      classes.push('expand');
    }

    if (this.responseObj.status) {
      this.responseObj.status === 200 ?
      classes.push('response-success') :
      classes.push('response-error');
    }
    return classes.join(' ');
  }

}
