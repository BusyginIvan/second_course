import { Injectable } from '@angular/core';
import {BreakpointObserver, BreakpointState} from "@angular/cdk/layout";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ScreenSizeService {
  private desktopObserver: Observable<BreakpointState>;
  private tabletObserver: Observable<BreakpointState>;
  private phoneObserver: Observable<BreakpointState>;

  isDesktop: boolean;
  isTablet: boolean;
  isPhone: boolean;

  constructor(private breakpointObserver: BreakpointObserver) {
    const desktopCondition = ['(min-width: 1040px)'];
    const tabletCondition = ['(min-width: 799px) and (max-width: 1039px)'];
    const phoneCondition = ['(max-width: 798px)'];

    this.desktopObserver = breakpointObserver.observe(desktopCondition);
    this.tabletObserver = breakpointObserver.observe(tabletCondition);
    this.phoneObserver = breakpointObserver.observe(phoneCondition);

    this.desktopObserver.subscribe(state => this.isDesktop = state.matches);
    this.tabletObserver.subscribe(state => this.isTablet = state.matches);
    this.phoneObserver.subscribe(state => this.isPhone = state.matches);

    this.isDesktop = breakpointObserver.isMatched(desktopCondition);
    this.isTablet = breakpointObserver.isMatched(tabletCondition);
    this.isPhone = breakpointObserver.isMatched(phoneCondition);
  }

  subscribeDesktop(runnable: Function) {
    this.desktopObserver.subscribe(state => { if(state.matches) runnable() });
  }

  subscribeTablet(runnable: Function) {
    this.tabletObserver.subscribe(state => { if(state.matches) runnable() });
  }

  subscribePhone(runnable: Function) {
    this.phoneObserver.subscribe(state => { if(state.matches) runnable() });
  }
}
